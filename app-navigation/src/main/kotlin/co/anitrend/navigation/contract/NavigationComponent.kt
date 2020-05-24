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

package co.anitrend.navigation.contract

import android.content.Context
import co.anitrend.navigation.extensions.loadIntentOrNull

/**
 * Intermediate construct for navigation components
 *
 * @param corePackage top level package path
 * @param className specific class name for component
 * @param packageName specific package name for the target class
 * excluding the application package name
 */
abstract class NavigationComponent(
    val corePackage: String = APP_PACKAGE,
    override val className: String,
    override val packageName: String
) : INavigationRouter, INavigationTarget {

    override val navRouterIntent = loadIntentOrNull()

    /**
     * Starts the target [navRouterIntent] for the implementation
     */
    operator fun invoke(context: Context?) {
        runCatching {
            context?.startActivity(navRouterIntent)
        }.exceptionOrNull()?.printStackTrace()
    }

    companion object {
        private const val APP_PACKAGE = "co.anitrend"
    }
}