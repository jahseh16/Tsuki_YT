package com.zionhuang.music.extensions

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.zionhuang.music.models.MediaMetadata
import com.zionhuang.music.db.entities.Song
import com.zionhuang.music.db.entities.SongEntity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log

// Silent Handler to ignore exceptions silently
val SilentHandler = CoroutineExceptionHandler { _, throwable ->
    Log.d("TsukiYT", "Silent exception", throwable)
}

// Flow collector extensions
fun <T> Flow<T>.collect(scope: CoroutineScope, block: suspend (T) -> Unit) {
    scope.launch {
        this@collect.collect { block(it) }
    }
}

fun <T> Flow<T>.collectLatest(scope: CoroutineScope, block: suspend (T) -> Unit) {
    scope.launch {
        this@collectLatest.collectLatest { block(it) }
    }
}

// Player extensions
val Player.mediaItems: List<MediaItem>
    get() {
        val list = mutableListOf<MediaItem>()
        for (i in 0 until mediaItemCount) {
            list.add(getMediaItemAt(i))
        }
        return list
    }

val Player.currentMetadata: MediaMetadata?
    get() {
        return currentMediaItem?.metadata
    }

fun Player.findNextMediaItemById(mediaId: String): MediaItem? {
    for (i in 0 until mediaItemCount) {
        val item = getMediaItemAt(i)
        if (item.mediaId == mediaId) return item
    }
    return null
}

fun Player.getQueueWindows(): List<Timeline.Window> {
    val timeline = currentTimeline
    if (timeline.isEmpty) return emptyList()
    val list = mutableListOf<Timeline.Window>()
    for (i in 0 until timeline.windowCount) {
        val window = Timeline.Window()
        timeline.getWindow(i, window)
        list.add(window)
    }
    return list
}

fun Player.getCurrentQueueIndex(): Int {
    return currentMediaItemIndex
}

// Toggle Play/Pause helper
fun Player.togglePlayPause() {
    if (playbackState == Player.STATE_IDLE) {
        prepare()
    }
    playWhenReady = !playWhenReady
}

fun Player.toggleRepeatMode() {
    repeatMode = when (repeatMode) {
        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
        Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
        Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
        else -> Player.REPEAT_MODE_OFF
    }
}

// MediaItem and Metadata conversions
val MediaItem.metadata: MediaMetadata?
    get() {
        val tag = localConfiguration?.tag
        if (tag is MediaMetadata) return tag
        // Fallback: convert mediaMetadata properties to our custom MediaMetadata
        return MediaMetadata(
            id = mediaId,
            title = mediaMetadata.title?.toString() ?: "",
            thumbnailUrl = mediaMetadata.artworkUri?.toString()
        )
    }

fun MediaMetadata.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(id)
        .setUri(id)
        .setMediaMetadata(
            androidx.media3.common.MediaMetadata.Builder()
                .setTitle(title)
                .setArtworkUri(thumbnailUrl?.let { android.net.Uri.parse(it) })
                .build()
        )
        // Store our custom MediaMetadata object in the tag for easy extraction
        .setTag(this)
        .build()
}

fun Song.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(id)
        .setUri(id)
        .setMediaMetadata(
            androidx.media3.common.MediaMetadata.Builder()
                .setTitle(song.title)
                .setArtworkUri(song.thumbnailUrl?.let { android.net.Uri.parse(it) })
                .build()
        )
        .setTag(
            MediaMetadata(
                id = id,
                title = song.title,
                duration = song.duration,
                thumbnailUrl = song.thumbnailUrl,
                artists = artists.map { com.zionhuang.music.db.entities.Artist(it) },
                album = album?.let { com.zionhuang.music.db.entities.Album(it) }
            )
        )
        .build()
}

fun Song.toMediaItem(path: String): MediaItem {
    return toMediaItem()
}

fun SongEntity.toggleLibrary(): SongEntity {
    return copy(inLibrary = if (inLibrary == null) System.currentTimeMillis() else null)
}

fun SongEntity.toggleLike(): SongEntity {
    return copy(liked = !liked)
}

// Generic toEnum helper
inline fun <reified T : Enum<T>> String.toEnum(defaultValue: T): T {
    return try {
        java.lang.Enum.valueOf(T::class.java, this)
    } catch (e: Exception) {
        defaultValue
    }
}

// List manipulation helpers
fun <T> MutableList<T>.move(fromIndex: Int, toIndex: Int) {
    if (fromIndex == toIndex) return
    val item = removeAt(fromIndex)
    add(toIndex, item)
}

fun <T> List<T>.reversed(reverse: Boolean): List<T> {
    return if (reverse) this.reversed() else this
}

fun String.toSQLiteQuery(): SupportSQLiteQuery {
    return SimpleSQLiteQuery(this)
}
