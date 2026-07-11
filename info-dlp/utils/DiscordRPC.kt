package com.zionhuang.music.utils

import android.content.Context
import com.zionhuang.music.db.entities.Song

class DiscordRPC(
    private val context: Context,
    private val key: String
) {
    fun isRpcRunning(): Boolean = false
    fun updateSong(song: Song?) {}
    fun closeRPC() {}
}
