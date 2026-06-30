package org.burmese.pomi.screen

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pomi.shared.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import pomi.shared.generated.resources.img_home_card

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    goToCreate: () -> Unit,
    goToGallery: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "splash")
    val cardScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = EaseInOut),
            repeatMode = Reverse
        ),
        label = "cardScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Color(0xFF23264A),
                    0.45f to Color(0xFF0C0D1A),
                    1f to Color(0xFF0C0D1A)
                )
            )
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier
                .size(220.dp, height = 300.dp)
                .graphicsLayer {
                    scaleX = cardScale
                    scaleY = cardScale
                },
            painter = painterResource(Res.drawable.img_home_card),
            contentDescription = "img_home_card"
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(248.dp)
                .background(
                    color = Color(0xFF1B1D3C),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(
                modifier = Modifier
                    .clip(RoundedCornerShape(99.dp))
                    .width(40.dp),
                thickness = 4.dp,
                color = Color(0xFF4D5084)
            )
        }
//        Image(
//            modifier = Modifier
//                .size(width = 208.dp, height = 289.dp)
//                .align(Alignment.Center)
//                .offset(y = (-22 + floatY).dp),
//            painter = painterResource(Res.drawable.img_card),
//            contentDescription = "img_card"
//        )
//        Row(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(start = 24.dp, end = 24.dp, bottom = 26.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Button(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (createIsPressed) {
//                        Color(0xFF2FB3AA)
//                    } else {
//                        Color(0xFF46d6cd)
//                    }
//                ),
//                shape = RoundedCornerShape(18.dp),
//                onClick = goToCreate,
//                interactionSource = createInteractionSource
//            ) {
//                PomiText(
//                    text = "✦ 카드 만들기",
//                    fontSize = (15.5).dp,
//                    color = Color(0xFF072B2A),
//                    fontWeight = FontWeight.Bold
//                )
//            }
//            Button(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (galleryIsPressed) {
//                        Color(0xFF13252F)
//                    } else {
//                        Color(0x0DFFFFFF)
//                    },
//                ),
//                border = BorderStroke(
//                    width = 1.dp,
//                    color = if (galleryIsPressed) {
//                        Color(0xFF1D4950)
//                    } else {
//                        Color(0x1FFFFFFF)
//                    }
//                ),
//                shape = RoundedCornerShape(18.dp),
//                onClick = {
//                    coroutineScope.launch {
//                        snackbarHostState.showSnackbar("아직 개발 중인 기능입니다")
//                    }
//                },
//                interactionSource = galleryInteractionSource
//            ) {
//                PomiText(
//                    text = "▦ 카드 앨범",
//                    fontSize = (15.5).dp,
//                    color = Color(0xFFE9EBF6),
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
    }
}