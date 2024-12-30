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

package co.anitrend.media.component.compose

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.common.genre.ui.compose.GenresListComponent
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.common.media.ui.compose.section.MediaSummarySection
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.compose.widget.ranking.RankingItems
import co.anitrend.common.tag.ui.compose.TagListItems
import co.anitrend.core.android.compose.design.BackIconButton
import co.anitrend.core.android.compose.design.image.AniTrendImage
import co.anitrend.core.android.compose.design.image.AniTrendImageDefaults
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.media.R
import co.anitrend.media.component.viewmodel.state.MediaState
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.model.common.IParam

@Composable
private fun MediaDetailContent(
    media: Media,
    accentColor: Color,
    onMediaDiscoverableItemClick: (IParam) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AniTrendImage(
            image = media.image,
            imageType = RequestImage.Media.ImageType.BANNER,
            onClick = onImageClick,
            modifier = AniTrendImageDefaults.BANNER_SIZE
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .absoluteOffset(y = (-16).dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MediaSummarySection(
                    media = media,
                    accentColor = accentColor,
                    onCoverClick = onImageClick,
                    modifier = Modifier
                        .absoluteOffset(y = (-16).dp)
                )
                RankingItems(
                    accentColor = accentColor,
                    rankings = media.rankings.toList(),
                    onClick = { rank, sorting ->
                        onMediaDiscoverableItemClick(
                            MediaDiscoverRouter.MediaDiscoverParam(
                                type = media.category.type,
                                format = media.format,
                                season = media.season,
                                seasonYear = if (rank.allTime != true && media.category.type == MediaType.ANIME) rank.year else null,
                                startDate_like = if (rank.allTime != true && media.category.type == MediaType.MANGA) "${rank.year}%" else null,
                                sort = sorting
                            )
                        )
                    }
                )
                GenresListComponent(
                    genres = media.genres as List<Genre>,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
                )
                MarkdownText(
                    synopsis = media,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )
                TagListItems(
                    accentColor = accentColor,
                    tags = media.tags as List<Tag>,
                    onMediaDiscoverableItemClick = onMediaDiscoverableItemClick
                )
            }
        }
    }
}

@Composable
fun MediaScreenContent(
    mediaState: MediaState,
    onMyAnimeListButtonClick: (String) -> Unit,
    onBookmarkButtonClick: (View, Media) -> Unit,
    onFavouriteButtonClick: (View, FavouriteTaskRouter.Param) -> Unit,
    onFloatingActionButtonClick: (Media) -> Unit,
    onMediaDiscoverableItemClick: (IParam) -> Unit,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onBackClick: () -> Unit,
) {
    val state = mediaState.model.observeAsState()
    val media: Media = state.value ?: return

    val accentColor = media.image.rememberAccentColor()

    val view = LocalView.current

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    BackIconButton(onBackClick = onBackClick)
                    media.siteUrl.myAnimeList?.also { url ->
                        IconButton(onClick = { onMyAnimeListButtonClick(url) }) {
                            Icon(painter = painterResource(R.drawable.ic_my_anime_list), contentDescription = "Open my anime list")
                        }
                    }
                    IconButton(onClick = { onBookmarkButtonClick(view, media) }) {
                        val isOnMyList = media.mediaList != null
                        Icon(
                            imageVector = if (isOnMyList) Icons.Rounded.BookmarkAdded else Icons.Rounded.BookmarkAdd,
                            contentDescription = if (isOnMyList) "Manage media listing" else "Add to list",
                        )
                    }
                    IconButton(onClick = {
                        val param = FavouriteTaskRouter.Param.MediaToggleParam(
                            id = media.id,
                            mediaType = media.category.type
                        )
                        onFavouriteButtonClick(view, param)
                    }) {
                        Icon(
                            imageVector = if (media.isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = if (media.isFavourite) "Remove from favourites" else "Add to favourites",
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onFloatingActionButtonClick(media) },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Rounded.Share, "Share")
                    }
                },
            )
        },
    ) { innerPadding ->
        MediaDetailContent(
            media = media,
            accentColor = accentColor,
            onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
            onImageClick = onImageClick,
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaDetailComponentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean
) {
    PreviewTheme(darkTheme = darkTheme) {
        MediaDetailContent(
            media = Media.Extended.empty(),
            accentColor = Color.DarkGray,
            onMediaDiscoverableItemClick = {},
            onImageClick = {}
        )
    }
}
