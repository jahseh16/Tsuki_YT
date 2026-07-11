package com.zionhuang.music.models

import java.io.Serializable
import com.zionhuang.music.db.entities.SongEntity
import com.zionhuang.music.db.entities.Artist
import com.zionhuang.music.db.entities.Album
import com.zionhuang.innertube.models.SongItem

data class MediaMetadata(
    val id: String,
    val title: String,
    val duration: Int = -1, // in seconds
    val thumbnailUrl: String? = null,
    val album: Album? = null,
    val artists: List<Artist> = emptyList(),
    val lyrics: String? = null
) : Serializable {
    fun toSongEntity(): SongEntity {
        return SongEntity(
            id = id,
            title = title,
            duration = duration,
            thumbnailUrl = thumbnailUrl,
            albumId = album?.id,
            albumName = album?.title
        )
    }
}

data class PersistQueue(
    val title: String?,
    val items: List<MediaMetadata>,
    val mediaItemIndex: Int,
    val position: Long
) : Serializable

fun SongItem.toMediaMetadata(): MediaMetadata {
    return MediaMetadata(
        id = this.id ?: "",
        title = this.title ?: "",
        duration = this.duration ?: -1,
        thumbnailUrl = this.thumbnailUrl,
        artists = this.artists?.map { com.zionhuang.music.db.entities.Artist(com.zionhuang.music.db.entities.ArtistEntity(id = it.id ?: "", name = it.name)) } ?: emptyList(),
        album = this.album?.let { com.zionhuang.music.db.entities.Album(com.zionhuang.music.db.entities.AlbumEntity(id = it.id ?: "", title = it.name)) }
    )
}
