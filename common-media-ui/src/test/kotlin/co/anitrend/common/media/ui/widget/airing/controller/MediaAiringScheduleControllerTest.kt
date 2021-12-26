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

package co.anitrend.common.media.ui.widget.airing.controller

import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class MediaAiringScheduleControllerTest {

    private val entity = mockk<Media>()

    @Before
    fun setUp() {
        every { entity.category } returns Media.Category.Anime.empty()
        every { entity.image } returns MediaImage.empty().copy(color = "#902F3A")
    }

    @Test
    fun getColor() {

    }

    @Test
    fun getSchedule() {

    }

    @Test
    fun shouldHideWidget() {

    }
}