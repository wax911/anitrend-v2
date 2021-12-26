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

package co.anitrend.data.relation.extensions

import co.anitrend.data.core.extensions.api
import co.anitrend.data.relation.api.RelationApiFactory
import co.anitrend.data.relation.datasource.local.IRelationStore
import org.koin.core.scope.Scope


internal inline fun <reified T> Scope.remoteSource(): T {
    val provider = get<RelationApiFactory>()
    return api(provider)
}

internal fun Scope.relationLocalSource() =
    get<IRelationStore>().relationDao()