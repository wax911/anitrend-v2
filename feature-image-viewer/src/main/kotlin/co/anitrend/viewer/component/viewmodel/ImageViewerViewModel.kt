/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.viewer.component.viewmodel

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel

class ImageViewerViewModel(
    private val downloadManager: DownloadManager?
) : ViewModel() {

    fun downloadImage(imageSource: String?) {
        val imageUri = Uri.parse(imageSource)
        val request = DownloadManager.Request(imageUri)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            imageUri.lastPathSegment
        )
        request.setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        downloadManager?.enqueue(request)
    }
}