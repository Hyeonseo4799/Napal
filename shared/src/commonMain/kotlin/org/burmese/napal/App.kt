package org.burmese.napal

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.burmese.napal.screen.SplashScreen

@Composable
@Preview
fun App() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onFinished = { showSplash = false })
    } else {
        // TODO: 메인 화면
    }
}