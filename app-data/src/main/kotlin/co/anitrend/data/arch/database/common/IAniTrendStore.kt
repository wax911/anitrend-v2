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

package co.anitrend.data.arch.database.common

import co.anitrend.data.airing.datasource.local.IAiringStore
import co.anitrend.data.auth.datasource.local.IAuthStore
import co.anitrend.data.genre.datasource.local.IMediaGenreStore
import co.anitrend.data.media.datasource.local.carousel.IMediaCarouselStore
import co.anitrend.data.media.datasource.local.media.IMediaStore
import co.anitrend.data.source.datasource.local.ISourceStore
import co.anitrend.data.tag.datasource.local.IMediaTagStore

internal interface IAniTrendStore : IMediaStore, IMediaGenreStore, IMediaTagStore,
    ISourceStore, IAuthStore, IAiringStore, IMediaCarouselStore {

    companion object {

        /** Binding types for [IAniTrendStore] */
        internal val BINDINGS = arrayOf(
            IMediaStore::class, IMediaTagStore::class, IMediaGenreStore::class,
            ISourceStore::class, IAuthStore::class, IAiringStore::class,
            IMediaCarouselStore::class
        )
    }
}