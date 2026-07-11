package com.zionhuang.innertube.models

import kotlinx.serialization.Serializable

sealed interface YTItem

@Serializable
data class Artist(
    val name: String,
    val id: String? = null
)

@Serializable
data class ArtistItem(
    val id: String,
    val name: String,
    val thumbnailUrl: String? = null,
    val info: String? = null
) : YTItem

@Serializable
data class AlbumItem(
    val browseId: String,
    val title: String,
    val subtitle: String? = null,
    val artists: List<Artist>? = null,
    val year: String? = null,
    val thumbnail: String? = null
) : YTItem

@Serializable
data class PlaylistItem(
    val playlistId: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val songCount: Int = 0,
    val author: Artist? = null,
    val description: String? = null
) : YTItem

@Serializable
data class SongItem(
    val id: String?,
    val title: String?,
    val duration: Int? = null,
    val thumbnailUrl: String? = null,
    val artists: List<Artist>? = null,
    val album: Album? = null,
    val isExplicit: Boolean = false
) : YTItem {
    @Serializable
    data class Album(
        val id: String?,
        val name: String
    )
}

@Serializable
data class SearchSuggestions(
    val queries: List<String> = emptyList(),
    val recommendedItems: List<YTItem> = emptyList()
)

@Serializable
data class AccountInfo(
    val name: String,
    val email: String? = null,
    val thumbnailUrl: String? = null
)

@Serializable
data class Context(
    val client: Client,
    val thirdParty: ThirdParty? = null
) {
    @Serializable
    data class Client(
        val clientName: String,
        val clientVersion: String,
        val hl: String = "en",
        val gl: String = "US"
    )

    @Serializable
    data class ThirdParty(
        val embedUrl: String
    )
}

@Serializable
data class BrowseEndpoint(
    val browseId: String,
    val params: String? = null
)

@Serializable
data class MusicNavigationButtonRenderer(
    val buttonText: Runs? = null,
    val clickCommand: NavigationEndpoint? = null,
    val solid: Solid? = null
) {
    @Serializable
    data class Solid(
        val leftStripeColor: Long? = null
    )
}

@Serializable
data class GridRenderer(
    val items: List<Item> = emptyList(),
    val continuations: List<Continuation>? = null,
    val header: GridHeader? = null
) {
    @Serializable
    data class Item(
        val musicTwoRowItemRenderer: MusicTwoRowItemRenderer? = null,
        val musicNavigationButtonRenderer: MusicNavigationButtonRenderer? = null
    )
    @Serializable
    data class GridHeader(
        val gridHeaderRenderer: GridHeaderRenderer? = null
    )
    @Serializable
    data class GridHeaderRenderer(
        val title: Runs? = null
    )
}

@Serializable
data class GridContinuation(
    val items: List<GridRenderer.Item> = emptyList(),
    val continuations: List<Continuation>? = null
)

@Serializable
data class MusicCarouselShelfRenderer(
    val header: Header? = null,
    val contents: List<Content>? = null
) {
    @Serializable
    data class Header(
        val musicCarouselShelfBasicHeaderRenderer: BasicHeader? = null
    )

    @Serializable
    data class BasicHeader(
        val title: Runs? = null,
        val moreContentButton: Button? = null
    )

    @Serializable
    data class Button(
        val buttonRenderer: ButtonRenderer? = null
    )

    @Serializable
    data class ButtonRenderer(
        val navigationEndpoint: NavigationEndpoint? = null
    )

    @Serializable
    data class Content(
        val musicTwoRowItemRenderer: MusicTwoRowItemRenderer? = null,
        val musicNavigationButtonRenderer: MusicNavigationButtonRenderer? = null
    )
}

@Serializable
data class WatchEndpoint(
    val videoId: String? = null,
    val playlistId: String? = null,
    val index: Int? = null,
    val params: String? = null,
    val playlistSetVideoId: String? = null
) {
    @Serializable
    data class WatchEndpointMusicSupportedConfigs(
        val watchEndpointMusicConfig: WatchEndpointMusicConfig? = null
    ) {
        @Serializable
        data class WatchEndpointMusicConfig(
            val musicVideoType: String? = null
        ) {
            companion object {
                const val MUSIC_VIDEO_TYPE_ATV = "MUSIC_VIDEO_TYPE_ATV"
            }
        }
    }
}

fun List<String>.oddElements(): List<String> {
    return this.filterIndexed { index, _ -> index % 2 != 0 }
}

@Serializable
data class Continuation(
    val nextContinuationData: NextContinuationData? = null,
    val reloadContinuationData: ReloadContinuationData? = null
) {
    @Serializable
    data class NextContinuationData(
        val continuation: String
    )
    @Serializable
    data class ReloadContinuationData(
        val continuation: String
    )
}

fun List<Continuation>?.getContinuation(): String? {
    return this?.mapNotNull { it.nextContinuationData?.continuation ?: it.reloadContinuationData?.continuation }?.firstOrNull()
}

@Serializable
data class SearchBody(
    val context: Context,
    val query: String? = null,
    val params: String? = null
)

@Serializable
data class PlayerBody(
    val context: Context,
    val videoId: String,
    val playlistId: String? = null
)

@Serializable
data class BrowseBody(
    val context: Context,
    val browseId: String? = null,
    val params: String? = null
)

@Serializable
data class NextBody(
    val context: Context,
    val videoId: String? = null,
    val playlistId: String? = null,
    val playlistSetVideoId: String? = null,
    val index: Int? = null,
    val params: String? = null,
    val continuation: String? = null
)

@Serializable
data class GetSearchSuggestionsBody(
    val context: Context,
    val input: String
)

@Serializable
data class GetQueueBody(
    val context: Context,
    val videoIds: List<String>? = null,
    val playlistId: String? = null
)

@Serializable
data class GetTranscriptBody(
    val context: Context,
    val params: String? = null
)

@Serializable
data class AccountMenuBody(
    val context: Context
)

// Shared Renderers & DTOs used across Response files

@Serializable
data class Runs(
    val runs: List<Run>? = null
) {
    fun joinToString(separator: String = ""): String {
        return runs?.joinToString(separator) { it.text }.orEmpty()
    }
}

@Serializable
data class Run(
    val text: String,
    val navigationEndpoint: NavigationEndpoint? = null
)

@Serializable
data class NavigationEndpoint(
    val browseEndpoint: BrowseEndpoint? = null,
    val watchEndpoint: WatchEndpoint? = null,
    val watchPlaylistEndpoint: WatchPlaylistEndpoint? = null,
    val shareEntityServiceEndpoint: ShareEntityServiceEndpoint? = null
)

@Serializable
data class WatchPlaylistEndpoint(
    val playlistId: String
)

@Serializable
data class ShareEntityServiceEndpoint(
    val serializedShareEntity: String
)

@Serializable
data class ThumbnailRenderer(
    val musicThumbnailRenderer: MusicThumbnailRenderer? = null
)

@Serializable
data class MusicThumbnailRenderer(
    val thumbnail: Thumbnail? = null
)

@Serializable
data class Thumbnail(
    val thumbnails: List<ThumbnailUrl>? = null
)

@Serializable
data class ThumbnailUrl(
    val url: String,
    val width: Int? = null,
    val height: Int? = null
)

@Serializable
data class Button(
    val buttonRenderer: ButtonRenderer? = null
)

@Serializable
data class ButtonRenderer(
    val navigationEndpoint: NavigationEndpoint? = null,
    val command: Command? = null
)

@Serializable
data class Command(
    val browseEndpoint: BrowseEndpoint? = null
)

@Serializable
data class Menu(
    val menuRenderer: MenuRenderer? = null
)

@Serializable
data class MenuRenderer(
    val items: List<MenuItem>? = null
)

@Serializable
data class MenuItem(
    val menuNavigationItemRenderer: MenuNavigationItemRenderer? = null,
    val toggleMenuServiceItemRenderer: ToggleMenuServiceItemRenderer? = null
)

@Serializable
data class MenuNavigationItemRenderer(
    val navigationEndpoint: NavigationEndpoint? = null,
    val icon: Icon? = null
)

@Serializable
data class ToggleMenuServiceItemRenderer(
    val defaultIcon: Icon? = null,
    val toggledIcon: Icon? = null
)

@Serializable
data class Icon(
    val iconType: String
)

@Serializable
data class Overlay(
    val musicItemThumbnailOverlayRenderer: MusicItemThumbnailOverlayRenderer? = null
)

@Serializable
data class MusicItemThumbnailOverlayRenderer(
    val content: OverlayContent? = null
)

@Serializable
data class OverlayContent(
    val musicPlayButtonRenderer: MusicPlayButtonRenderer? = null
)

@Serializable
data class MusicPlayButtonRenderer(
    val playNavigationEndpoint: NavigationEndpoint? = null
)

@Serializable
data class ActiveAccountHeaderRenderer(
    val accountName: Runs? = null,
    val email: Runs? = null,
    val accountPhoto: ThumbnailRenderer? = null
) {
    fun toAccountInfo(): AccountInfo {
        return AccountInfo(
            name = accountName?.runs?.firstOrNull()?.text.orEmpty(),
            email = email?.runs?.firstOrNull()?.text,
            thumbnailUrl = accountPhoto?.musicThumbnailRenderer?.thumbnail?.thumbnails?.firstOrNull()?.url
        )
    }
}

@Serializable
data class TwoColumnBrowseResultsRenderer(
    val tabs: List<Tab>? = null,
    val secondaryContents: SecondaryContents? = null
)

@Serializable
data class SecondaryContents(
    val sectionListRenderer: SectionListRenderer? = null
)

@Serializable
data class Tab(
    val tabRenderer: TabRenderer? = null
)

@Serializable
data class TabRenderer(
    val content: Content? = null,
    val endpoint: NavigationEndpoint? = null,
    val title: String? = null,
    val selected: Boolean? = null
)

@Serializable
data class Content(
    val sectionListRenderer: SectionListRenderer? = null,
    val musicQueueRenderer: MusicQueueRenderer? = null
)

@Serializable
data class SectionListRenderer(
    val contents: List<SectionContent>? = null,
    val continuations: List<Continuation>? = null
)

@Serializable
data class SectionContent(
    val musicShelfRenderer: MusicShelfRenderer? = null,
    val musicCarouselShelfRenderer: MusicCarouselShelfRenderer? = null,
    val musicCardShelfRenderer: MusicCardShelfRenderer? = null,
    val musicPlaylistShelfRenderer: MusicPlaylistShelfRenderer? = null,
    val musicDescriptionShelfRenderer: MusicDescriptionShelfRenderer? = null,
    val musicResponsiveHeaderRenderer: MusicResponsiveHeaderRenderer? = null,
    val gridRenderer: GridRenderer? = null,
    val musicNavigationButtonRenderer: MusicNavigationButtonRenderer? = null
)

@Serializable
data class MusicImmersiveHeaderRenderer(
    val title: Runs? = null,
    val description: Runs? = null,
    val thumbnail: ThumbnailRenderer? = null,
    val playButton: PlayButton? = null,
    val startRadioButton: StartRadioButton? = null
)

@Serializable
data class MusicVisualHeaderRenderer(
    val title: Runs? = null,
    val thumbnail: ThumbnailRenderer? = null
)

@Serializable
data class MusicHeaderRenderer(
    val title: Runs? = null
)

@Serializable
data class PlayButton(
    val buttonRenderer: ButtonRenderer? = null
)

@Serializable
data class StartRadioButton(
    val buttonRenderer: ButtonRenderer? = null
)

@Serializable
data class SingleColumnMusicWatchNextResultsRenderer(
    val tabbedRenderer: TabbedRenderer? = null
)

@Serializable
data class TabbedRenderer(
    val watchNextTabbedResultsRenderer: WatchNextTabbedResultsRenderer? = null
)

@Serializable
data class WatchNextTabbedResultsRenderer(
    val tabs: List<Tab> = emptyList()
)

@Serializable
data class MusicQueueRenderer(
    val header: MusicQueueHeader? = null,
    val content: MusicQueueContent? = null
)

@Serializable
data class MusicQueueHeader(
    val musicQueueHeaderRenderer: MusicQueueHeaderRenderer? = null
)

@Serializable
data class MusicQueueHeaderRenderer(
    val subtitle: Runs? = null
)

@Serializable
data class MusicQueueContent(
    val playlistPanelRenderer: PlaylistPanelRenderer? = null
)

@Serializable
data class PlaylistPanelRenderer(
    val contents: List<PlaylistPanelContent> = emptyList(),
    val continuations: List<Continuation>? = null
)

@Serializable
data class PlaylistPanelContent(
    val playlistPanelVideoRenderer: PlaylistPanelVideoRenderer? = null,
    val automixPreviewVideoRenderer: AutomixPreviewVideoRenderer? = null
)

@Serializable
data class PlaylistPanelVideoRenderer(
    val videoId: String? = null,
    val title: Runs? = null,
    val longBylineText: Runs? = null,
    val thumbnail: ThumbnailRenderer? = null,
    val lengthText: Runs? = null,
    val selected: Boolean = false,
    val navigationEndpoint: NavigationEndpoint? = null
)

@Serializable
data class AutomixPreviewVideoRenderer(
    val content: AutomixContent? = null
)

@Serializable
data class AutomixContent(
    val automixPlaylistVideoRenderer: AutomixPlaylistVideoRenderer? = null
)

@Serializable
data class AutomixPlaylistVideoRenderer(
    val navigationEndpoint: NavigationEndpoint? = null
)

@Serializable
data class ContinuationContents(
    val musicPlaylistShelfContinuation: MusicPlaylistShelfContinuation? = null,
    val playlistPanelContinuation: PlaylistPanelRenderer? = null,
    val gridContinuation: GridContinuation? = null
)

@Serializable
data class TabbedSearchResultsRenderer(
    val tabs: List<Tab>? = null
)

@Serializable
data class MusicShelfRenderer(
    val contents: List<MusicShelfContent>? = null,
    val title: Runs? = null,
    val continuations: List<Continuation>? = null
)

@Serializable
data class MusicShelfContent(
    val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer? = null
)

@Serializable
data class MusicShelfContinuation(
    val contents: List<MusicShelfContent>? = null,
    val continuations: List<Continuation>? = null
)

@Serializable
data class MusicPlaylistShelfRenderer(
    val contents: List<MusicPlaylistShelfContent>? = null,
    val continuations: List<Continuation>? = null
)

@Serializable
data class MusicPlaylistShelfContent(
    val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer? = null
)

@Serializable
data class MusicPlaylistShelfContinuation(
    val contents: List<MusicPlaylistShelfContent>? = null,
    val continuations: List<Continuation>? = null
)

@Serializable
data class MusicDescriptionShelfRenderer(
    val description: Runs? = null
)

@Serializable
data class MusicResponsiveHeaderRenderer(
    val title: Runs? = null,
    val straplineTextOne: Runs? = null,
    val subtitle: Runs? = null,
    val thumbnail: ThumbnailRenderer? = null,
    val secondSubtitle: Runs? = null,
    val buttons: List<Button>? = null
)

@Serializable
data class MusicEditablePlaylistDetailHeaderRenderer(
    val header: MusicPlaylistHeaderRenderer? = null
)

@Serializable
data class MusicPlaylistHeaderRenderer(
    val musicResponsiveHeaderRenderer: MusicResponsiveHeaderRenderer? = null
)

@Serializable
data class MusicTwoRowItemRenderer(
    val title: Runs? = null,
    val navigationEndpoint: NavigationEndpoint? = null,
    val thumbnailRenderer: ThumbnailRenderer? = null,
    val subtitle: Runs? = null,
    val menu: Menu? = null
)

@Serializable
data class MusicResponsiveListItemRenderer(
    val flexColumns: List<FlexColumn>? = null,
    val fixedColumns: List<FixedColumn>? = null,
    val navigationEndpoint: NavigationEndpoint? = null,
    val thumbnail: ThumbnailRenderer? = null,
    val menu: Menu? = null,
    val overlay: Overlay? = null,
    val itemIndex: Int? = null,
    val checklistItemRenderer: ChecklistItemRenderer? = null
)

@Serializable
data class FlexColumn(
    val musicResponsiveListItemFlexColumnRenderer: FlexColumnRenderer? = null
)

@Serializable
data class FlexColumnRenderer(
    val text: Runs? = null
)

@Serializable
data class FixedColumn(
    val musicResponsiveListItemFixedColumnRenderer: FixedColumnRenderer? = null
)

@Serializable
data class FixedColumnRenderer(
    val text: Runs? = null
)

@Serializable
data class ChecklistItemRenderer(
    val selected: Boolean? = null
)

@Serializable
data class MusicCardShelfRenderer(
    val title: Runs? = null,
    val subtitle: Runs? = null,
    val thumbnail: ThumbnailRenderer? = null,
    val buttons: List<Button>? = null,
    val menu: Menu? = null
)
