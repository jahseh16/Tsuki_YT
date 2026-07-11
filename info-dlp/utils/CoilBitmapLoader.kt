package com.zionhuang.music.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.media3.common.util.BitmapLoader
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.SettableFuture
import kotlinx.coroutines.CoroutineScope

class CoilBitmapLoader(
    private val context: Context,
    private val scope: CoroutineScope
) : BitmapLoader {
    override fun supportsMimeType(mimeType: String): Boolean {
        return true
    }

    override fun decodeBitmap(data: ByteArray): ListenableFuture<Bitmap> {
        val future = SettableFuture.create<Bitmap>()
        future.setException(UnsupportedOperationException("Stub implementation"))
        return future
    }

    override fun loadBitmap(uri: Uri): ListenableFuture<Bitmap> {
        val future = SettableFuture.create<Bitmap>()
        future.setException(UnsupportedOperationException("Stub implementation"))
        return future
    }
}
