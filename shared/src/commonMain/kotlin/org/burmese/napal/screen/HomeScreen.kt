package org.burmese.napal.screen

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import napal.shared.generated.resources.Res
import napal.shared.generated.resources.img_card
import org.burmese.napal.component.NapalText
import org.burmese.napal.component.drawGradientBackground
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    goToCreate: () -> Unit,
    goToGallery: () -> Unit
) {
    val createInteractionSource = remember { MutableInteractionSource() }
    val galleryInteractionSource = remember { MutableInteractionSource() }

    val createIsPressed by createInteractionSource.collectIsPressedAsState()
    val galleryIsPressed by galleryInteractionSource.collectIsPressedAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val transition = rememberInfiniteTransition(label = "splash")
    val floatY by transition.animateFloat(
        initialValue = 0.dp.value,
        targetValue = (-20).dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = EaseInOut),
            repeatMode = Reverse
        ),
        label = "floaty"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawGradientBackground() }
            .then(modifier)
    ) {
        Image(
            modifier = Modifier
                .size(width = 208.dp, height = 289.dp)
                .align(Alignment.Center)
                .offset(y = (-22 + floatY).dp),
            painter = painterResource(Res.drawable.img_card),
            contentDescription = "img_card"
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 26.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (createIsPressed) {
                        Color(0xFF2FB3AA)
                    } else {
                        Color(0xFF46d6cd)
                    }
                ),
                shape = RoundedCornerShape(18.dp),
                onClick = goToCreate,
                interactionSource = createInteractionSource
            ) {
                NapalText(
                    text = "✦ 카드 만들기",
                    fontSize = (15.5).dp,
                    color = Color(0xFF072B2A),
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (galleryIsPressed) {
                        Color(0xFF13252F)
                    } else {
                        Color(0x0DFFFFFF)
                    },
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (galleryIsPressed) {
                        Color(0xFF1D4950)
                    } else {
                        Color(0x1FFFFFFF)
                    }
                ),
                shape = RoundedCornerShape(18.dp),
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("아직 개발 중인 기능입니다")
                    }
                },
                interactionSource = galleryInteractionSource
            ) {
                NapalText(
                    text = "▦ 카드 앨범",
                    fontSize = (15.5).dp,
                    color = Color(0xFFE9EBF6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState,
        )
    }
}