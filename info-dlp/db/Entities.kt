package com.zionhuang.music.db.entities

import androidx.room.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val duration: Int = -1, // in seconds
    val thumbnailUrl: String? = null,
    val albumId: String? = null,
    val albumName: String? = null,
    val liked: Boolean = false,
    val totalPlayTime: Long = 0L,
    val isTrash: Boolean = false,
    @ColumnInfo(name = "download_state") val downloadState: Int = 0,
    @ColumnInfo(name = "create_date") val createDate: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "modify_date") val modifyDate: LocalDateTime = LocalDateTime.now(),
    val inLibrary: Long? = null
) : Serializable

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val thumbnailUrl: String? = null,
    val bannerUrl: String? = null,
    val description: String? = null,
    val createDate: LocalDateTime = LocalDateTime.now(),
    val lastUpdateTime: LocalDateTime = LocalDateTime.now(),
    val bookmarkedAt: LocalDateTime? = null
) {
    val isYouTubeArtist: Boolean
        get() = id.startsWith("UC") || id.startsWith("FMy") || id.startsWith("VL") || id.startsWith("MP") || id.startsWith("LA") || id.startsWith("AL") || id != "local"

    companion object {
        fun generateArtistId(): String = UUID.randomUUID().toString()
    }
}

@Entity(tableName = "album")
data class AlbumEntity(
    @PrimaryKey val id: String,
    val title: String,
    val year: Int? = null,
    val thumbnailUrl: String? = null,
    val songCount: Int = 0,
    val duration: Int = 0,
    val createDate: LocalDateTime = LocalDateTime.now(),
    val lastUpdateTime: LocalDateTime = LocalDateTime.now(),
    val bookmarkedAt: LocalDateTime? = null
)

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val author: String? = null,
    val authorId: String? = null,
    val year: Int? = null,
    val thumbnailUrl: String? = null,
    val createDate: LocalDateTime = LocalDateTime.now(),
    val lastUpdateTime: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        const val LIKED_PLAYLIST_ID = "liked_songs"
        const val DOWNLOADED_PLAYLIST_ID = "downloaded_songs"
        fun generatePlaylistId(): String = UUID.randomUUID().toString()
    }
}

@Entity(tableName = "song_artist_map", primaryKeys = ["songId", "artistId"])
data class SongArtistMap(
    val songId: String,
    val artistId: String,
    val position: Int
)

@Entity(tableName = "song_album_map", primaryKeys = ["songId", "albumId"])
data class SongAlbumMap(
    val songId: String,
    val albumId: String,
    @ColumnInfo(name = "index") val index: Int? = null
)

@Entity(tableName = "album_artist_map", primaryKeys = ["albumId", "artistId"])
data class AlbumArtistMap(
    val albumId: String,
    val artistId: String,
    val order: Int
)

@Entity(tableName = "playlist_song_map")
data class PlaylistSongMap(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playlistId: String,
    val songId: String,
    val position: Int
)

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val query: String
)

@Entity(tableName = "format")
data class FormatEntity(
    @PrimaryKey val id: String,
    val itag: Int?,
    val mimeType: String,
    val codecs: String,
    val bitrate: Int?,
    val sampleRate: Int?,
    val contentLength: Long,
    val loudnessDb: Double?
)

@Entity(tableName = "lyrics")
data class LyricsEntity(
    @PrimaryKey val id: String,
    val lyrics: String?
) {
    companion object {
        const val LYRICS_NOT_FOUND = "LYRICS_NOT_FOUND"
    }
}

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val songId: String,
    val timestamp: LocalDateTime,
    val playTime: Long
)

@Entity(tableName = "related_song_map", primaryKeys = ["songId", "relatedSongId"])
data class RelatedSongMap(
    val songId: String,
    val relatedSongId: String
)

// Relationships / views used in DAO and UI
data class Song(
    @Embedded val song: SongEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SongArtistMap::class,
            parentColumn = "songId",
            entityColumn = "artistId"
        )
    )
    val artists: List<ArtistEntity> = emptyList(),
    
    @Relation(
        parentColumn = "albumId",
        entityColumn = "id"
    )
    val album: AlbumEntity? = null
) : Serializable {
    val id: String get() = song.id
}

data class Artist(
    @Embedded val artist: ArtistEntity,
    val songCount: Int = 0
) : Serializable {
    constructor(artistEntity: ArtistEntity) : this(artist = artistEntity)
    val id: String get() = artist.id
    val name: String get() = artist.name
    val thumbnailUrl: String? get() = artist.thumbnailUrl
}

data class Album(
    @Embedded val album: AlbumEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumArtistMap::class,
            parentColumn = "albumId",
            entityColumn = "artistId"
        )
    )
    val artists: List<ArtistEntity> = emptyList()
) : Serializable {
    constructor(albumEntity: AlbumEntity) : this(album = albumEntity)
    val id: String get() = album.id
    val title: String get() = album.title
}

data class AlbumWithSongs(
    @Embedded val album: Album,
    
    @Relation(
        entity = SongEntity::class,
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val songs: List<Song> = emptyList()
) : Serializable

data class Playlist(
    @Embedded val playlist: PlaylistEntity,
    val songCount: Int = 0
) : Serializable {
    @Ignore var thumbnails: List<String> = emptyList()
    val id: String get() = playlist.id
}

data class PlaylistSong(
    @Embedded val playlistSongMap: PlaylistSongMap,
    
    @Relation(
        entity = SongEntity::class,
        parentColumn = "songId",
        entityColumn = "id"
    )
    val song: Song
) : Serializable

data class EventWithSong(
    @Embedded val event: Event,
    
    @Relation(
        entity = SongEntity::class,
        parentColumn = "songId",
        entityColumn = "id"
    )
    val song: Song
) : Serializable

@DatabaseView("SELECT * FROM song_artist_map ORDER BY position")
data class SortedSongArtistMap(
    val songId: String,
    val artistId: String,
    val position: Int
)

@DatabaseView("SELECT * FROM song_album_map")
data class SortedSongAlbumMap(
    val songId: String,
    val albumId: String,
    val position: Int
)

@DatabaseView("SELECT * FROM playlist_song_map WHERE position <= 3 ORDER BY position")
data class PlaylistSongMapPreview(
    val id: Int,
    val playlistId: String,
    val songId: String,
    val position: Int
)
