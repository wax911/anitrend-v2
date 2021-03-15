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

package co.anitrend.domain.staff.model

import co.anitrend.domain.staff.enums.StaffSort

sealed class StaffParam {

    /** [Staff query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by the staff id
     * @param search Filter by search query
     * @param id_not Filter by the staff id
     * @param id_in Filter by the staff id
     * @param id_not_in Filter by the staff id
     * @param sort The order the results will be returned in
     */
    data class Find(
        val id: Long,
        val search: String,
        val id_not: Long,
        val id_in: List<Long>?,
        val id_not_in: List<Long>?,
        val sort: List<StaffSort>?
    ) : StaffParam()
}
