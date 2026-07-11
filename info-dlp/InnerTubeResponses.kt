package com.zionhuang.innertube.models.response

import kotlinx.serialization.Serializable
import com.zionhuang.innertube.models.*

@Serializable
class PipedResponse

@Serializable
data class AccountMenuResponse(
    val actions: List<Action> = emptyList()
) {
    @Serializable
    data class Action(
        val openPopupAction: OpenPopupAction
    )
    @Serializable
    data class OpenPopupAction(
        val popup: Popup
    )
    @Serializable
    data class Popup(
        val multiPageMenuRenderer: MultiPageMenuRenderer
    )
    @Serializable
    data class MultiPageMenuRenderer(
        val header: Header? = null
    ) {
        @Serializable
        data class Header(
            val activeAccountHeaderRenderer: ActiveAccountHeaderRenderer? = null
        )
    }
}

@Serializable
data class BrowseResponse(
    val contents: BrowseContents? = null,
    val header: BrowseHeader? = null,
    val microformat: Microformat? = null,
    val continuationContents: ContinuationContents? = null
) {
    @Serializable
    data class BrowseContents(
        val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer? = null,
        val singleColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer? = null,
        val sectionListRenderer: SectionListRenderer? = null
    )
    @Serializable
    data class BrowseHeader(
        val musicImmersiveHeaderRenderer: MusicImmersiveHeaderRenderer? = null,
        val musicVisualHeaderRenderer: MusicVisualHeaderRenderer? = null,
        val musicHeaderRenderer: MusicHeaderRenderer? = null,
        val musicDetailHeaderRenderer: MusicResponsiveHeaderRenderer? = null,
        val musicEditablePlaylistDetailHeaderRenderer: MusicEditablePlaylistDetailHeaderRenderer? = null
    )
    @Serializable
    data class Microformat(
        val microformatDataRenderer: MicroformatDataRenderer? = null
    )
    @Serializable
    data class MicroformatDataRenderer(
        val urlCanonicalToPageUrl: String? = null
    )
}

@Serializable
data class GetQueueResponse(
    val queueDatas: List<QueueData> = emptyList()
) {
    @Serializable
    data class QueueData(
        val content: QueueContent
    ) {
        @Serializable
        data class QueueContent(
            val playlistPanelVideoRenderer: PlaylistPanelVideoRenderer? = null
        )
    }
}

@Serializable
data class GetSearchSuggestionsResponse(
    val contents: List<SearchSuggestionsSection>? = null
) {
    @Serializable
    data class SearchSuggestionsSection(
        val searchSuggestionsSectionRenderer: SearchSuggestionsSectionRenderer? = null
    )
    @Serializable
    data class SearchSuggestionsSectionRenderer(
        val contents: List<SearchSuggestionContent>? = null
    )
    @Serializable
    data class SearchSuggestionContent(
        val searchSuggestionRenderer: SearchSuggestionRenderer? = null,
        val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer? = null
    )
    @Serializable
    data class SearchSuggestionRenderer(
        val suggestion: Runs? = null
    )
}

@Serializable
data class GetTranscriptResponse(
    val actions: List<Action>? = null
) {
    @Serializable
    data class Action(
        val updateEngagementPanelAction: UpdateEngagementPanelAction? = null
    )
    @Serializable
    data class UpdateEngagementPanelAction(
        val content: Content? = null
    )
    @Serializable
    data class Content(
        val transcriptRenderer: TranscriptRenderer? = null
    )
    @Serializable
    data class TranscriptRenderer(
        val body: Body? = null
    )
    @Serializable
    data class Body(
        val transcriptBodyRenderer: TranscriptBodyRenderer? = null
    )
    @Serializable
    data class TranscriptBodyRenderer(
        val cueGroups: List<CueGroup> = emptyList()
    )
    @Serializable
    data class CueGroup(
        val transcriptCueGroupRenderer: TranscriptCueGroupRenderer
    )
    @Serializable
    data class TranscriptCueGroupRenderer(
        val cues: List<Cue> = emptyList()
    )
    @Serializable
    data class Cue(
        val transcriptCueRenderer: TranscriptCueRenderer
    )
    @Serializable
    data class TranscriptCueRenderer(
        val startOffsetMs: Long,
        val cue: CueText
    )
    @Serializable
    data class CueText(
        val simpleText: String
    )
}

@Serializable
data class NextResponse(
    val contents: NextContents? = null,
    val continuationContents: NextContinuationContents? = null
) {
    @Serializable
    data class NextContents(
        val singleColumnMusicWatchNextResultsRenderer: SingleColumnMusicWatchNextResultsRenderer? = null
    )
    @Serializable
    data class NextContinuationContents(
        val playlistPanelContinuation: PlaylistPanelRenderer? = null
    )
}

@Serializable
data class SearchResponse(
    val contents: Contents? = null,
    val continuationContents: ContinuationContents? = null
) {
    @Serializable
    data class Contents(
        val tabbedSearchResultsRenderer: TabbedSearchResultsRenderer? = null
    )
    @Serializable
    data class ContinuationContents(
        val musicShelfContinuation: MusicShelfContinuation? = null
    )
}

@Serializable
data class PlayerResponse(
    val playabilityStatus: PlayabilityStatus? = null,
    val videoDetails: VideoDetails? = null,
    val streamingData: StreamingData? = null,
    val playerConfig: PlayerConfig? = null
) {
    @Serializable
    data class PlayabilityStatus(
        val status: String? = null,
        val reason: String? = null
    )

    @Serializable
    data class VideoDetails(
        val videoId: String? = null,
        val title: String? = null,
        val author: String? = null,
        val lengthSeconds: String? = null,
        val thumbnail: ThumbnailRenderer? = null
    )

    @Serializable
    data class PlayerConfig(
        val audioConfig: AudioConfig? = null
    ) {
        @Serializable
        class AudioConfig
    }
}

@Serializable
data class StreamingData(
    val adaptiveFormats: List<Format> = emptyList(),
    val expiresInSeconds: String? = null
) {
    @Serializable
    data class Format(
        val itag: Int,
        val mimeType: String,
        val bitrate: Long? = null,
        val audioSampleRate: String? = null,
        val contentLength: String? = null,
        val url: String? = null,
        val signatureCipher: String? = null
    ) {
        val isAudio: Boolean get() = mimeType.startsWith("audio/")
    }
}
