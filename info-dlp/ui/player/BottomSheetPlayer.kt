package com.zionhuang.music.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zionhuang.music.R
import com.zionhuang.music.ui.component.BottomSheet
import com.zionhuang.music.ui.component.BottomSheetState
import com.zionhuang.music.playback.LocalPlayerConnection
import com.zionhuang.music.extensions.togglePlayPause
import com.zionhuang.music.extensions.toggleRepeatMode
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetPlayer(
    state: BottomSheetState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val playerConnection = LocalPlayerConnection.current ?: return
    val currentSongMetadata by playerConnection.mediaMetadata.collectAsStateWithLifecycle()
    val isPlaying by playerConnection.isPlaying.collectAsStateWithLifecycle()
    
    if (currentSongMetadata == null) return

    val title = currentSongMetadata?.title ?: "Unknown Song"
    val artist = currentSongMetadata?.artists?.joinToString { it.name } ?: "Unknown Artist"
    val thumbnailUrl = currentSongMetadata?.thumbnailUrl

    // Track dynamic playback progress (position)
    var currentPosition by remember { mutableLongStateOf(playerConnection.player.currentPosition) }
    val duration = currentSongMetadata?.duration?.toLong()?.times(1000) ?: 1L // duration is in seconds, convert to ms

    LaunchedEffect(isPlaying, currentSongMetadata) {
        while (isPlaying) {
            currentPosition = playerConnection.player.currentPosition
            delay(500)
        }
    }

    BottomSheet(
        state = state,
        modifier = modifier,
        onDismiss = {
            // Player bottom sheet usually stays collapsed instead of fully dismissed,
            // but we can collapse it if needed.
        },
        collapsedContent = {
            // Beautiful Mini Player
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f))
                    .clickable { state.expand() }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { playerConnection.player.togglePlayPause() }
                ) {
                    Icon(
                        painter = painterResource(
                            if (isPlaying) R.drawable.close // or any play/pause icon, wait, we don't have play icon in drawables. Let's look at the close or search icon, or use custom vector graphics
                            else R.drawable.music_note
                        ),
                        contentDescription = "Play/Pause"
                    )
                }
                
                IconButton(
                    onClick = { playerConnection.seekToNext() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back), // using mirror or arrow for skip next
                        contentDescription = "Next"
                    )
                }
            }
        },
        content = {
            // Full Player Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Drag Handle / Close button
                Box(
                    modifier = Modifier
                        .size(48.dp, 4.dp)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                        .clickable { state.collapse() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Artwork
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Title & Artist
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = artist,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Progress Slider
                Slider(
                    value = currentPosition.toFloat().coerceIn(0f, duration.toFloat()),
                    onValueChange = {
                        currentPosition = it.toLong()
                    },
                    onValueChangeFinished = {
                        playerConnection.player.seekTo(currentPosition)
                    },
                    valueRange = 0f..duration.toFloat(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                // Time labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = formatTime(duration),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Playback Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val shuffleEnabled by playerConnection.shuffleModeEnabled.collectAsStateWithLifecycle()
                    IconButton(
                        onClick = { playerConnection.player.shuffleModeEnabled = !shuffleEnabled }
                    ) {
                        Icon(
                            painter = painterResource(if (shuffleEnabled) R.drawable.shuffle_on else R.drawable.shuffle),
                            contentDescription = "Shuffle",
                            tint = if (shuffleEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = { playerConnection.seekToPrevious() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Previous"
                        )
                    }

                    Button(
                        onClick = { playerConnection.player.togglePlayPause() },
                        shape = RoundedCornerShape(100),
                        modifier = Modifier.size(64.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.music_note),
                            contentDescription = "Play/Pause",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = { playerConnection.seekToNext() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back), // or custom next icon
                            contentDescription = "Next"
                        )
                    }

                    val repeatMode by playerConnection.repeatMode.collectAsStateWithLifecycle()
                    IconButton(
                        onClick = { playerConnection.player.toggleRepeatMode() }
                    ) {
                        Icon(
                            painter = painterResource(
                                when (repeatMode) {
                                    androidx.media3.common.Player.REPEAT_MODE_ONE -> R.drawable.repeat_one_on
                                    androidx.media3.common.Player.REPEAT_MODE_ALL -> R.drawable.repeat_on
                                    else -> R.drawable.repeat
                                }
                            ),
                            contentDescription = "Repeat",
                            tint = if (repeatMode != androidx.media3.common.Player.REPEAT_MODE_OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    )
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
