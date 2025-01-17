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

package co.anitrend.data.feed.episode.usecase

import co.anitrend.data.feed.episode.EpisodeDetailInteractor
import co.anitrend.data.feed.episode.EpisodeDetailRepository
import co.anitrend.data.feed.episode.EpisodePagedInteractor
import co.anitrend.data.feed.episode.EpisodePagedRepository
import co.anitrend.data.feed.episode.EpisodeSyncInteractor
import co.anitrend.data.feed.episode.EpisodeSyncRepository

internal sealed class EpisodeInteractor {

    class Detail(
        repository: EpisodeDetailRepository
    ) : EpisodeDetailInteractor(repository)

    class Paged(
        repository: EpisodePagedRepository
    ) : EpisodePagedInteractor(repository)

    class Sync(
        repository: EpisodeSyncRepository
    ) : EpisodeSyncInteractor(repository)
}
