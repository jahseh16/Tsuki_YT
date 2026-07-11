package com.zionhuang.music.playback

import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope

class SleepTimer(
    private val scope: CoroutineScope,
    private val player: Player
) : Player.Listener {
    var isActive: Boolean = false
    var pauseWhenSongEnd: Boolean = false
    var triggerTime: Long = 0L

    fun clear() {
        isActive = false
        pauseWhenSongEnd = false
        triggerTime = 0L
    }

    fun start(minutes: Int) {
        if (minutes == -1) {
            pauseWhenSongEnd = true
            isActive = true
        } else {
            triggerTime = System.currentTimeMillis() + (minutes * 60 * 1000L)
            isActive = true
        }
    }
}
