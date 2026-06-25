package org.burmese.napal.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Restart
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import napal.shared.generated.resources.Res
import napal.shared.generated.resources.img_splash
import org.burmese.napal.component.NapalText
import org.jetbrains.compose.resources.painterResource
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun SplashScreen(onFinished: () -> Unit = {}) {
    // 임시 딜레이 처리
    LaunchedEffect(Unit) {
        delay(2L.toDuration(DurationUnit.SECONDS))
        onFinished.invoke()
    }
    val transition = rememberInfiniteTransition(label = "splash")
    val cardRotationY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = Restart
        ),
        label = "cardRotationY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0B15)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(width = 118.dp, height = 160.dp)
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationY = cardRotationY
                    cameraDistance = 12 * density
                },
            painter = painterResource(Res.drawable.img_splash),
            contentDescription = "img_splash"
        )
        NapalText(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            text = "N A P A L",
            fontSize = 12.dp,
            color = Color(0xFF43465E),
            fontWeight = FontWeight.SemiBold,
        )
    }
}