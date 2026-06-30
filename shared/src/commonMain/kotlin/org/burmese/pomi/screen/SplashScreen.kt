package org.burmese.pomi.screen

import androidx.compose.animation.core.EaseInOut
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
import pomi.shared.generated.resources.Res
import org.burmese.pomi.component.PomiText
import org.jetbrains.compose.resources.painterResource
import pomi.shared.generated.resources.img_placeholder_400x600
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun SplashScreen(onFinished: () -> Unit = {}) {
    // TODO 로딩 딜레이로 변경
    LaunchedEffect(Unit) {
        delay(2L.toDuration(DurationUnit.SECONDS))
        onFinished.invoke()
    }

    val transition = rememberInfiniteTransition(label = "splash")
    val cardRotationY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = EaseInOut),
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
                .size(width = 100.dp, height = 150.dp)
                .graphicsLayer {
                    rotationY = cardRotationY
                    rotationZ = -10f
                    cameraDistance = 12 * density
                },
            painter = painterResource(Res.drawable.img_placeholder_400x600),
            contentDescription = "img_placeholder_splash"
        )
        PomiText(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            text = "P O M I",
            fontSize = 14.dp,
            color = Color(0xFFEFEAFF),
            fontWeight = FontWeight.SemiBold,
        )
    }
}