package com.zionhuang.music.playback.queues

import androidx.media3.common.MediaItem
import com.zionhuang.music.models.MediaMetadata

object EmptyQueue : Queue {
    override val preloadItem: MediaMetadata? = null

    override suspend fun getInitialStatus(): Queue.Status {
        return Queue.Status(null, emptyList(), 0, 0L)
    }

    override fun hasNextPage(): Boolean = false

    override suspend fun nextPage(): List<MediaItem> = emptyList()
}

class ListQueue(
    private val title: String?,
    private val items: List<MediaItem>,
    private val startIndex: Int = 0,
    private val position: Long = 0L
) : Queue {
    override val preloadItem: MediaMetadata? = null

    override suspend fun getInitialStatus(): Queue.Status {
        return Queue.Status(
            title = title,
            items = items,
            mediaItemIndex = startIndex,
            position = position
        )
    }

    override fun hasNextPage(): Boolean = false

    override suspend fun nextPage(): List<MediaItem> = emptyList()
}

fun List<MediaItem>.filterExplicit(hideExplicit: Boolean): List<MediaItem> {
    if (!hideExplicit) return this
    return this // Simple fallback/stub: do no aggressive filtering
}

fun Queue.Status.filterExplicit(hideExplicit: Boolean): Queue.Status {
    if (!hideExplicit) return this
    return copy(items = items.filterExplicit(hideExplicit))
}
