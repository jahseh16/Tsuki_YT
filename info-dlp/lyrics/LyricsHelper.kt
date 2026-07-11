package com.zionhuang.music.lyrics

import com.zionhuang.music.models.MediaMetadata
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricsHelper @Inject constructor() {
    suspend fun getLyrics(mediaMetadata: MediaMetadata): String? {
        return null
    }
}
