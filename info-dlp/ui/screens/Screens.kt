package com.zionhuang.music.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment

sealed class Screens(val route: String, val titleId: Int, val iconId: Int) {
    object Home : Screens("home", com.zionhuang.music.R.string.home, com.zionhuang.music.R.drawable.library_music)
    object Songs : Screens("songs", com.zionhuang.music.R.string.songs, com.zionhuang.music.R.drawable.music_note)
    object Artists : Screens("artists", com.zionhuang.music.R.string.artists, com.zionhuang.music.R.drawable.artist)
    object Albums : Screens("albums", com.zionhuang.music.R.string.albums, com.zionhuang.music.R.drawable.album)
    object Playlists : Screens("playlists", com.zionhuang.music.R.string.playlists, com.zionhuang.music.R.drawable.queue_music)

    companion object {
        val MainScreens = listOf(Home, Songs, Artists, Albums, Playlists)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navigationBuilder(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    latestVersionName: Result<String>?
) {
    NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) {
            PlaceholderScreen("Home Screen")
        }
        composable(Screens.Songs.route) {
            PlaceholderScreen("Songs Screen")
        }
        composable(Screens.Artists.route) {
            PlaceholderScreen("Artists Screen")
        }
        composable(Screens.Albums.route) {
            PlaceholderScreen("Albums Screen")
        }
        composable(Screens.Playlists.route) {
            PlaceholderScreen("Playlists Screen")
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}
