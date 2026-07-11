package com.zionhuang.innertube.pages

import com.zionhuang.innertube.models.*

class AlbumPage(
    val album: AlbumItem,
    val songs: List<SongItem> = emptyList(),
    val otherVersions: List<AlbumItem> = emptyList()
) {
    companion object {
        fun fromMusicResponsiveListItemRenderer(renderer: MusicResponsiveListItemRenderer): SongItem? {
            return renderer.toSongItem()
        }
        fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): AlbumItem? {
            return ArtistItemsPage.fromMusicTwoRowItemRenderer(renderer) as? AlbumItem
        }
    }
}

class ArtistPage(
    val artist: ArtistItem,
    val sections: List<Section> = emptyList(),
    val description: String? = null
) {
    class Section(
        val title: String,
        val items: List<YTItem>,
        val moreEndpoint: BrowseEndpoint? = null
    )

    companion object {
        fun fromSectionListRendererContent(content: SectionContent): Section? {
            val title = content.musicShelfRenderer?.title?.runs?.firstOrNull()?.text
                ?: content.musicCarouselShelfRenderer?.header?.musicCarouselShelfBasicHeaderRenderer?.title?.runs?.firstOrNull()?.text
                ?: return null
            val items = content.musicShelfRenderer?.contents?.mapNotNull {
                it.musicResponsiveListItemRenderer?.let(SearchPage::toYTItem)
            } ?: content.musicCarouselShelfRenderer?.contents?.mapNotNull {
                it.musicTwoRowItemRenderer?.let(ArtistItemsPage::fromMusicTwoRowItemRenderer)
            } ?: return null
            val moreEndpoint = content.musicCarouselShelfRenderer?.header?.musicCarouselShelfBasicHeaderRenderer?.moreContentButton?.buttonRenderer?.navigationEndpoint?.browseEndpoint
            return Section(title, items, moreEndpoint)
        }
    }
}

class ArtistItemsPage(
    val title: String,
    val items: List<YTItem> = emptyList(),
    val continuation: String? = null
) {
    companion object {
        fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): YTItem? {
            val id = renderer.navigationEndpoint?.browseEndpoint?.browseId 
                ?: renderer.navigationEndpoint?.watchEndpoint?.videoId 
                ?: return null
            val title = renderer.title?.runs?.firstOrNull()?.text ?: return null
            val thumbnail = renderer.thumbnailRenderer?.musicThumbnailRenderer?.thumbnail?.thumbnails?.lastOrNull()?.url
            if (renderer.navigationEndpoint?.browseEndpoint != null) {
                if (id.startsWith("FSM") || id.startsWith("MP")) {
                    return PlaylistItem(playlistId = id, title = title, thumbnailUrl = thumbnail)
                }
                return AlbumItem(browseId = id, title = title, thumbnail = thumbnail)
            } else if (renderer.navigationEndpoint?.watchEndpoint != null) {
                return SongItem(id = id, title = title, thumbnailUrl = thumbnail)
            }
            return null
        }

        fun fromMusicResponsiveListItemRenderer(renderer: MusicResponsiveListItemRenderer): SongItem? {
            return renderer.toSongItem()
        }
    }
}

class ArtistItemsContinuationPage(
    val items: List<YTItem> = emptyList(),
    val continuation: String? = null
)

class PlaylistPage(
    val playlist: PlaylistItem,
    val songs: List<SongItem> = emptyList(),
    val songsContinuation: String? = null,
    val continuation: String? = null
) {
    companion object {
        fun fromMusicResponsiveListItemRenderer(renderer: MusicResponsiveListItemRenderer): SongItem? {
            return renderer.toSongItem()
        }
    }
}

class PlaylistContinuationPage(
    val songs: List<SongItem> = emptyList(),
    val continuation: String? = null
)

class RelatedPage(
    val songs: List<SongItem> = emptyList(),
    val albums: List<AlbumItem> = emptyList(),
    val artists: List<ArtistItem> = emptyList(),
    val playlists: List<PlaylistItem> = emptyList()
) {
    companion object {
        fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): YTItem? {
            return ArtistItemsPage.fromMusicTwoRowItemRenderer(renderer)
        }
    }
}

class SearchPage {
    companion object {
        fun toYTItem(renderer: MusicResponsiveListItemRenderer): YTItem? {
            val id = renderer.navigationEndpoint?.browseEndpoint?.browseId 
                ?: renderer.navigationEndpoint?.watchEndpoint?.videoId 
                ?: return null
            val title = renderer.flexColumns?.firstOrNull()?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.firstOrNull()?.text ?: return null
            val thumbnail = renderer.thumbnail?.musicThumbnailRenderer?.thumbnail?.thumbnails?.lastOrNull()?.url
            
            if (id.startsWith("MPRE") || id.startsWith("FSM")) {
                return AlbumItem(browseId = id, title = title, thumbnail = thumbnail)
            } else if (id.startsWith("VL") || id.startsWith("PL")) {
                return PlaylistItem(playlistId = id, title = title, thumbnailUrl = thumbnail)
            } else if (id.startsWith("UC")) {
                return ArtistItem(id = id, name = title, thumbnailUrl = thumbnail)
            } else if (renderer.navigationEndpoint?.watchEndpoint != null) {
                return renderer.toSongItem()
            }
            return null
        }
    }
}

class SearchResult(
    val items: List<YTItem>,
    val continuation: String? = null
)

class SearchSuggestionPage {
    companion object {
        fun fromMusicResponsiveListItemRenderer(renderer: MusicResponsiveListItemRenderer): YTItem? {
            return SearchPage.toYTItem(renderer)
        }
    }
}

class SearchSummary(
    val title: String,
    val items: List<YTItem>
)

class SearchSummaryPage(
    val summaries: List<SearchSummary>
) {
    companion object {
        fun fromMusicCardShelfRenderer(renderer: MusicCardShelfRenderer): YTItem? {
            val id = renderer.buttons?.firstOrNull()?.buttonRenderer?.command?.browseEndpoint?.browseId ?: return null
            val title = renderer.title?.runs?.firstOrNull()?.text ?: return null
            val thumbnail = renderer.thumbnail?.musicThumbnailRenderer?.thumbnail?.thumbnails?.lastOrNull()?.url
            if (id.startsWith("UC")) {
                return ArtistItem(id = id, name = title, thumbnailUrl = thumbnail)
            } else {
                return AlbumItem(browseId = id, title = title, thumbnail = thumbnail)
            }
        }

        fun fromMusicResponsiveListItemRenderer(renderer: MusicResponsiveListItemRenderer): YTItem? {
            return SearchPage.toYTItem(renderer)
        }
    }
}

class NextResult(
    val title: String?,
    val items: List<SongItem>,
    val currentIndex: Int?,
    val lyricsEndpoint: BrowseEndpoint?,
    val relatedEndpoint: BrowseEndpoint?,
    val continuation: String?,
    val endpoint: WatchEndpoint
)

class NextPage {
    companion object {
        fun fromPlaylistPanelVideoRenderer(renderer: PlaylistPanelVideoRenderer): SongItem? {
            val id = renderer.videoId ?: return null
            val title = renderer.title?.runs?.firstOrNull()?.text ?: return null
            val duration = renderer.lengthText?.runs?.firstOrNull()?.text?.let { parseDuration(it) }
            val artists = renderer.longBylineText?.runs?.filter { 
                it.navigationEndpoint?.browseEndpoint?.browseId?.startsWith("UC") == true || 
                it.navigationEndpoint?.browseEndpoint?.browseId?.startsWith("FSM") == true 
            }?.map {
                Artist(name = it.text, id = it.navigationEndpoint?.browseEndpoint?.browseId)
            }
            val album = renderer.longBylineText?.runs?.find { 
                it.navigationEndpoint?.browseEndpoint?.browseId?.startsWith("MPRE") == true 
            }?.let {
                SongItem.Album(id = it.navigationEndpoint?.browseEndpoint?.browseId, name = it.text)
            }
            return SongItem(
                id = id,
                title = title,
                duration = duration,
                thumbnailUrl = renderer.thumbnail?.musicThumbnailRenderer?.thumbnail?.thumbnails?.lastOrNull()?.url,
                artists = artists,
                album = album
            )
        }
    }
}

// Extension and Helper Functions

fun ThumbnailRenderer.getThumbnailUrl(): String? {
    return this.musicThumbnailRenderer?.thumbnail?.thumbnails?.lastOrNull()?.url
}

fun MusicResponsiveListItemRenderer.toSongItem(): SongItem? {
    val id = this.navigationEndpoint?.watchEndpoint?.videoId ?: return null
    val title = this.flexColumns?.firstOrNull()?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.firstOrNull()?.text ?: return null
    val duration = this.fixedColumns?.firstOrNull()?.musicResponsiveListItemFixedColumnRenderer?.text?.runs?.firstOrNull()?.text?.let { parseDuration(it) }
    
    val runs = this.flexColumns.getOrNull(1)?.musicResponsiveListItemFlexColumnRenderer?.text?.runs
    val artists = runs?.filter { it.navigationEndpoint?.browseEndpoint?.browseId?.startsWith("UC") == true }?.map {
        Artist(name = it.text, id = it.navigationEndpoint?.browseEndpoint?.browseId)
    }
    val album = runs?.find { it.navigationEndpoint?.browseEndpoint?.browseId?.startsWith("MPRE") == true }?.let {
        SongItem.Album(id = it.navigationEndpoint?.browseEndpoint?.browseId, name = it.text)
    }
    
    return SongItem(
        id = id,
        title = title,
        duration = duration,
        thumbnailUrl = this.thumbnail?.musicThumbnailRenderer?.thumbnail?.thumbnails?.lastOrNull()?.url,
        artists = artists,
        album = album
    )
}

private fun parseDuration(durationStr: String): Int {
    val parts = durationStr.split(":")
    return when (parts.size) {
        3 -> parts[0].toInt() * 3600 + parts[1].toInt() * 60 + parts[2].toInt()
        2 -> parts[0].toInt() * 60 + parts[1].toInt()
        1 -> parts[0].toInt()
        else -> 0
    }
}

class NewReleaseAlbumPage {
    companion object {
        fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): AlbumItem? {
            return AlbumPage.fromMusicTwoRowItemRenderer(renderer)
        }
    }
}

class ExplorePage(
    val newReleaseAlbums: List<AlbumItem> = emptyList(),
    val moodAndGenres: List<MoodAndGenres.Item> = emptyList()
)

class HomePage(
    val sections: List<Section> = emptyList()
) {
    class Section(
        val title: String,
        val items: List<YTItem>,
        val moreEndpoint: BrowseEndpoint? = null
    ) {
        companion object {
            fun fromMusicCarouselShelfRenderer(renderer: MusicCarouselShelfRenderer): Section? {
                val title = renderer.header?.musicCarouselShelfBasicHeaderRenderer?.title?.runs?.firstOrNull()?.text ?: return null
                val items = renderer.contents?.mapNotNull {
                    it.musicTwoRowItemRenderer?.let(ArtistItemsPage::fromMusicTwoRowItemRenderer)
                } ?: return null
                val moreEndpoint = renderer.header.musicCarouselShelfBasicHeaderRenderer.moreContentButton?.buttonRenderer?.navigationEndpoint?.browseEndpoint
                return Section(title, items, moreEndpoint)
            }
        }
    }
}

class MoodAndGenres(
    val title: String,
    val items: List<Item>
) {
    class Item(
        val title: String,
        val endpoint: BrowseEndpoint,
        val stripeColor: String? = null
    )

    companion object {
        fun fromMusicNavigationButtonRenderer(renderer: MusicNavigationButtonRenderer): Item? {
            val title = renderer.buttonText?.runs?.firstOrNull()?.text ?: return null
            val endpoint = renderer.clickCommand?.browseEndpoint ?: return null
            val color = renderer.solid?.leftStripeColor?.toString()
            return Item(title, endpoint, color)
        }

        fun fromSectionListRendererContent(content: SectionContent): MoodAndGenres? {
            val gridRenderer = content.gridRenderer ?: return null
            val title = gridRenderer.header?.gridHeaderRenderer?.title?.runs?.firstOrNull()?.text ?: ""
            val items = gridRenderer.items.mapNotNull {
                it.musicNavigationButtonRenderer?.let { r ->
                    fromMusicNavigationButtonRenderer(r)
                }
            }
            return MoodAndGenres(title, items)
        }
    }
}

class BrowseResult(
    val title: String?,
    val items: List<Item> = emptyList()
) {
    class Item(
        val title: String?,
        val items: List<YTItem> = emptyList()
    )
}
