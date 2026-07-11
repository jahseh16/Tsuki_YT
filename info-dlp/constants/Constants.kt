package com.zionhuang.music.constants

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.ui.unit.dp

// Layout Heights
val AppBarHeight = 64.dp
val MiniPlayerHeight = 64.dp
val NavigationBarHeight = 80.dp

// Animation Specs
val NavigationBarAnimationSpec: AnimationSpec<androidx.compose.ui.unit.Dp> = spring()

// Datastore Keys
const val DarkModeKey = "dark_mode"
const val DefaultOpenTabKey = "default_open_tab"
const val DisableScreenshotKey = "disable_screenshot"
const val DynamicThemeKey = "dynamic_theme"
const val PauseSearchHistoryKey = "pause_search_history"
const val PureBlackKey = "pure_black"
const val SearchSourceKey = "search_source"
const val StopMusicOnTaskClearKey = "stop_music_on_task_clear"
const val TranslateLyricsKey = "translate_lyrics"
const val AudioNormalizationKey = "audio_normalization"
const val AudioQualityKey = "audio_quality"
const val AutoLoadMoreKey = "auto_load_more"
const val AutoSkipNextOnErrorKey = "auto_skip_next_on_error"
const val DiscordTokenKey = "discord_token"
const val EnableDiscordRPCKey = "enable_discord_rpc"
const val HideExplicitKey = "hide_explicit"
const val PauseListenHistoryKey = "pause_listen_history"
const val PersistentQueueKey = "persistent_queue"
const val PlayerVolumeKey = "player_volume"
const val RepeatModeKey = "repeat_mode"
const val ShowLyricsKey = "show_lyrics"
const val SkipSilenceKey = "skip_silence"

// Sort Types
enum class SongSortType {
    CREATE_DATE, NAME, ARTIST, PLAY_TIME
}

enum class AlbumSortType {
    CREATE_DATE, NAME, ARTIST, YEAR, SONG_COUNT, LENGTH, PLAY_TIME
}

enum class ArtistSortType {
    CREATE_DATE, NAME, SONG_COUNT, PLAY_TIME
}

enum class PlaylistSortType {
    CREATE_DATE, NAME, SONG_COUNT
}

enum class ArtistSongSortType {
    CREATE_DATE, NAME, PLAY_TIME
}

// Search Source
enum class SearchSource {
    LOCAL, ONLINE
}

// Audio Quality
enum class AudioQuality {
    AUTO, HIGH, LOW
}

// Media Session Constants
object MediaSessionConstants {
    const val ACTION_TOGGLE_LIBRARY = "ACTION_TOGGLE_LIBRARY"
    const val ACTION_TOGGLE_LIKE = "ACTION_TOGGLE_LIKE"
    const val ACTION_TOGGLE_SHUFFLE = "ACTION_TOGGLE_SHUFFLE"
    const val ACTION_TOGGLE_REPEAT_MODE = "ACTION_TOGGLE_REPEAT_MODE"

    val CommandToggleLibrary = androidx.media3.session.SessionCommand(ACTION_TOGGLE_LIBRARY, android.os.Bundle.EMPTY)
    val CommandToggleLike = androidx.media3.session.SessionCommand(ACTION_TOGGLE_LIKE, android.os.Bundle.EMPTY)
    val CommandToggleShuffle = androidx.media3.session.SessionCommand(ACTION_TOGGLE_SHUFFLE, android.os.Bundle.EMPTY)
    val CommandToggleRepeatMode = androidx.media3.session.SessionCommand(ACTION_TOGGLE_REPEAT_MODE, android.os.Bundle.EMPTY)
}
