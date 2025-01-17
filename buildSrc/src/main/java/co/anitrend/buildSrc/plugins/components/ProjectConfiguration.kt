/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.buildSrc.plugins.components

import co.anitrend.buildSrc.extensions.*
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.io.File

private fun Project.configureBuildFlavours() {
    baseAppExtension().run {
        flavorDimensions.add("default")
        productFlavors {
            create("google") {
                dimension = "default"
                isDefault = true
            }
            create("github") {
                dimension = "default"
                versionNameSuffix = "-github"
            }
        }
        applicationVariants.all {
            outputs.map { it as BaseVariantOutputImpl }.forEach { output ->
                val original = output.outputFileName
                output.outputFileName = original
            }
        }
    }
}

@Suppress("UnstableApiUsage")
private fun DefaultConfig.applyAdditionalConfiguration(project: Project) {
    if (project.isAppModule()) {
        applicationId = "co.anitrend"
        project.baseAppExtension().run {
            buildFeatures {
                viewBinding = true
                compose = true
            }
        }
    }
    else
        consumerProguardFiles.add(File("consumer-rules.pro"))

    if (!project.matchesAppModule() && !project.matchesTaskModule()) {
        // checking app module again since the group for app modules is
        // `app-` while the main app is just `app`
        if (!project.isAppModule()) {
            project.logger.lifecycle("Applying view binding feature for module -> ${project.path}")
            project.libraryExtension().buildFeatures {
                viewBinding = true
                if (project.hasComposeSupport())
                    compose = true
            }
        }

        project.logger.lifecycle("Applying vector drawables configuration for module -> ${project.path}")
        vectorDrawables.useSupportLibrary = true
    }
}

private fun Project.configureLint() = baseAppExtension().run {
    lint {
        abortOnError = false
        ignoreWarnings = false
        ignoreTestSources = true
    }
}

internal fun Project.configureAndroid(): Unit = baseExtension().run {
    compileSdkVersion(35)
    defaultConfig {
        minSdk = 24
        targetSdk = 35
        versionCode = props[PropertyTypes.CODE].toInt()
        versionName = props[PropertyTypes.VERSION]
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applyAdditionalConfiguration(project)
    }

    if (isAppModule()) {
        configureLint()
        configureBuildFlavours()
        createSigningConfiguration(this)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            isTestCoverageEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro",
                "../proguard-common.pro"
            )
            if (project.file(".config/keystore.properties").exists())
                signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro",
                "../proguard-common.pro"
            )
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/NOTICE.*")
        resources.excludes.add("META-INF/LICENSE*")
        // Exclude potential duplicate kotlin_module files
        resources.excludes.add("META-INF/*kotlin_module")
        // Exclude consumer proguard files
        resources.excludes.add("META-INF/proguard/*")
        // Exclude AndroidX version files
        resources.excludes.add("META-INF/*.version")
        // Exclude the Firebase/Fabric/other random properties files
        resources.excludes.add("META-INF/*.properties")
        resources.excludes.add("/*.properties")
        resources.excludes.add("fabric/*.properties")
    }

    sourceSets {
        map { androidSourceSet ->
            androidSourceSet.java.srcDir(
                "src/${androidSourceSet.name}/kotlin"
            )
        }
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType(KotlinJvmCompile::class.java) {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    tasks.withType(KotlinCompilationTask::class.java) {
        val compilerArgumentOptions = mutableListOf(
            "-opt-in=kotlin.ExperimentalStdlibApi",
        )

        if (hasCoroutineSupport()) {
            compilerArgumentOptions.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            compilerArgumentOptions.add("-opt-in=kotlinx.coroutines.FlowPreview")
        }

        if (hasComposeSupport()) {
            compilerArgumentOptions.add("-opt-in=androidx.compose.foundation.ExperimentalFoundationApi")
            compilerArgumentOptions.add("-opt-in=androidx.compose.material.ExperimentalMaterialApi")
        }

        if (isAppModule() || isCoreModule()) {
            compilerArgumentOptions.apply {
                add("-opt-in=org.koin.core.component.KoinApiExtension")
                add("-opt-in=org.koin.viewmodel.KoinInternalApi")
                add("-opt-in=org.koin.core.KoinExperimentalAPI")
            }
        }

        compilerOptions {
            allWarningsAsErrors.set(false)
            // Filter out modules that won't be using coroutines
            freeCompilerArgs.addAll(compilerArgumentOptions)
        }
    }

    tasks.withType(Test::class.java) {
        useJUnitPlatform()

        maxHeapSize = "1G"

        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }

    // Disabling experimental language version, causing issues with KAPT + Room
    //tasks.withType(KotlinCompilationTask::class.java)
    //    .configureEach {
    //        compilerOptions
    //            .languageVersion
    //            .set(KotlinVersion.KOTLIN_1_9)
    //    }

    tasks.register("makeProguard") {
        val projectDirectory = project.layout.projectDirectory
        val buildDirectory = project.layout.buildDirectory
        val proguardFile = projectDirectory.file("proguard-rules.pro").asFile
        val missingRules = buildDirectory.file("outputs/mapping/release/missing_rules.txt").get().asFile
        if (proguardFile.exists()) {
            if (missingRules.exists() && missingRules.length() > 0) {
                val contents = missingRules.readText()
                val existing = proguardFile.readText()
                if (proguardFile.length() < 1) {
                    logger.lifecycle("Creating proguard-rules.pro $proguardFile with contents from $missingRules")
                    proguardFile.writeText(contents)
                } else {
                    if (contents != existing) {
                        logger.lifecycle("Updating proguard-rules.pro $proguardFile with contents from $missingRules")
                        proguardFile.appendText(contents)
                    } else {
                        logger.lifecycle("$proguardFile contents are identical to $missingRules")
                    }
                }
            } else {
                logger.lifecycle("missing_rules.txt does not exist in $missingRules")
            }
        } else {
            logger.lifecycle("Creating new proguard-rules.pro file in: $proguardFile")
            proguardFile.createNewFile()
        }
    }
}
