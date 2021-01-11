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

package co.anitrend.data.media

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.arch.controller.graphql.GraphQLController
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.page.MediaPageModel
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.interactor.MediaUseCase


internal typealias MediaPagedCombinedController = GraphQLController<MediaPageModel, List<MediaEntity>>
internal typealias MediaPagedNetworkController = GraphQLController<MediaPageModel, List<Media>>

typealias MediaInteractor = MediaUseCase<DataState<PagedList<Media>>>