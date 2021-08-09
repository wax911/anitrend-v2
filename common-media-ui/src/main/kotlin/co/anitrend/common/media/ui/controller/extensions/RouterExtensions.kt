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

package co.anitrend.common.media.ui.controller.extensions

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import co.anitrend.arch.extension.ext.snackBar
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import com.google.android.material.snackbar.Snackbar

internal fun View.startMediaScreenFor(entity: Media) {
    MediaRouter.startActivity(
        context = context,
        navPayload = MediaRouter.Param(
            id = entity.id,
            type = entity.category.type
        ).asNavPayload()
    )
}

internal fun View.openMediaListSheetFor(
    entity: Media,
    settings: Settings
): Boolean {
    if (settings.isAuthenticated.value) {
        val controller = context as FragmentActivity
        val fragmentItem = FragmentItem(
            fragment = MediaListEditorRouter.forSheet(),
            parameter = MediaListEditorRouter.Param(
                mediaId = entity.id,
                mediaType = entity.category.type,
                scoreFormat = settings.scoreFormat.value
            ).asBundle()
        )
        val dialog = fragmentItem.fragmentByTagOrNew(controller) as DialogFragment
        dialog.show(controller.supportFragmentManager, fragmentItem.tag())
        return true
    }
    snackBar(
        text = resources.getString(R.string.label_text_authentication_required)
    ).show()
    return false
}