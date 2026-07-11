package com.zionhuang.music.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zionhuang.innertube.models.SongItem
import com.zionhuang.music.R
import com.zionhuang.music.playback.LocalPlayerConnection
import com.zionhuang.music.models.toMediaMetadata
import com.zionhuang.music.extensions.toMediaItem
import coil.compose.AsyncImage

@Composable
fun YouTubeSongMenu(
    song: SongItem,
    navController: NavController,
    onDismiss: () -> Unit
) {
    val playerConnection = LocalPlayerConnection.current ?: return
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = song.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(
                    text = song.title ?: "Unknown Title",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = song.artists?.joinToString { it.name } ?: "Unknown Artist",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        HorizontalDivider()
        
        Spacer(modifier = Modifier.height(8.dp))
        
        val items = listOf(
            Triple("Play next", R.drawable.queue_music) {
                playerConnection.playNext(song.toMediaMetadata().toMediaItem())
                onDismiss()
            },
            Triple("Add to queue", R.drawable.library_add) {
                playerConnection.addToQueue(song.toMediaMetadata().toMediaItem())
                onDismiss()
            }
        )
        
        items.forEach { (text, iconRes, action) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = action)
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(text = text, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
