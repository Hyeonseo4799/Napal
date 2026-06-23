package org.burmese.napal.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import coil3.compose.AsyncImage
import io.ktor.client.request.forms.formData
import org.burmese.napal.component.NapalHeader
import org.burmese.napal.component.NapalText
import org.burmese.napal.component.drawGradientBackground
import org.burmese.napal.model.Card

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    card: Card,
    onBack: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val rotX = remember { Animatable(0f) }
    val rotY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .drawBehind { drawGradientBackground() }
            .fillMaxSize()
            .then(modifier)
    ) {
        // 이미지 정보가 없는 경우
        if (card.byteArray == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NapalText(
                        text = "카드 정보를 찾을 수 없어요",
                        fontSize = 14.dp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF8A8DA6)
                    )
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF46d6cd)
                        ),
                        onClick = onBack
                    ) {
                        NapalText(
                            text = "이전으로",
                            fontSize = 14.dp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF072B2A)
                        )
                    }
                }
            }
        } else {
            NapalHeader(
                title = "내 카드",
                onBack = onBack
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = 224.dp, height = 310.dp)
                    .graphicsLayer {
                        rotationX = rotX.value
                        rotationY = rotY.value
                        cameraDistance = 8 * density
                    }
                    .clip(RoundedCornerShape(24.dp))
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val pos = event.changes.firstOrNull()?.position
                                if (pos != null && event.changes.any { it.pressed }) {
                                    val tiltY = (((pos.x / size.width).coerceIn(0f, 1f)) - 0.5f) * 30f
                                    val tiltX = -(((pos.y / size.height).coerceIn(0f, 1f)) - 0.5f) * 30f
                                    scope.launch { rotY.snapTo(tiltY) }
                                    scope.launch { rotX.snapTo(tiltX) }
                                } else {
                                    scope.launch { rotY.animateTo(0f, spring(dampingRatio = 0.6f)) }
                                    scope.launch { rotX.animateTo(0f, spring(dampingRatio = 0.6f)) }
                                }
                            }
                        }
                    }
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = card.byteArray,
                    contentDescription = "img_card",
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    NapalText(
                        text = card.name,
                        fontSize = 21.dp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFEEF0F8),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    NapalText(
                        text = card.message,
                        fontSize = 12.dp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC8CBE0),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    FlowRow(
                        maxLines = 3,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        card.tag.split(",").map { it.trim() }.forEach {
                            Box(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0x46D6CD52),
                                        shape = RoundedCornerShape(99.dp)
                                    )
                                    .background(
                                        color = Color(0x2446D6CD),
                                        shape = RoundedCornerShape(99.dp)
                                    )
                                    .padding(horizontal = 10.dp)
                                    .height(20.dp)
                            ) {
                                NapalText(
                                    text = it,
                                    fontSize = 11.dp,
                                    color = Color(0xFFA9E3DD),
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }

                    }
                }
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(0.dp), // 잘림 방지
                onClick = { showBottomSheet = true },
            ) {
                NapalText(
                    text = "✦ AI 페인팅",
                    fontWeight = FontWeight.Bold,
                    fontSize = (15.5).dp,
                    color = Color(0xFF072B2A)
                )
            }
        }
    }
}