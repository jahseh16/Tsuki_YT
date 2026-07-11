package com.zionhuang.music.ui.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Resize YouTube/InnerTube image urls
fun String.resize(width: Int, height: Int): String {
    if (this.contains("=s") || this.contains("=w")) {
        return this.replace(Regex("=s\\d+.*"), "=w$width-h$height-c")
                   .replace(Regex("=w\\d+-h\\d+.*"), "=w$width-h$height-c")
    }
    return this
}

// NavController extension to pop to root/main screen
fun NavController.backToMain() {
    while (previousBackStackEntry != null) {
        popBackStack()
    }
}

// CornerBasedShape extension to keep only top corners rounded
fun CornerBasedShape.top(): CornerBasedShape {
    return this.copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )
}

// TopAppBar scroll behaviors and states
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun appBarScrollBehavior(canScroll: () -> Boolean): TopAppBarScrollBehavior {
    return TopAppBarDefaults.enterAlwaysScrollBehavior(
        canScroll = canScroll
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarState.resetHeightOffset() {
    heightOffset = 0f
}
