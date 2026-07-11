package com.zionhuang.music.playback

import androidx.media3.exoplayer.offline.Download
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadUtil @Inject constructor() {
    val downloads: StateFlow<Map<String, Download>> = MutableStateFlow(emptyMap())
}
