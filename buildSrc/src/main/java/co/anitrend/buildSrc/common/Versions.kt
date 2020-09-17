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

package co.anitrend.buildSrc.common

object Versions {

    const val compileSdk = 29
    const val targetSdk = 29
    const val minSdk = 21

     /**
      * **RR**_X.Y.Z_
      * > **RR** reserved for build flavours and **X.Y.Z** follow the [versionName] convention
      */
    const val versionCode = 1020018

    /**
     * Naming schema: X.Y.Z-variant##
     * > **X**(Major).**Y**(Minor).**Z**(Patch)
     */
    const val versionName = "2.0.0-alpha18"

    const val mockk = "1.10.0"
    const val junit = "4.13"

    const val timber = "4.7.1"
    const val threeTenBp = "1.2.4"

    const val debugDB = "1.0.6"
    const val treesSence = "0.3.2"

    const val liquidSwipe = "1.3"

    const val prettyTime = "4.0.4.Final"
    const val scalingImageView = "3.10.0"
}