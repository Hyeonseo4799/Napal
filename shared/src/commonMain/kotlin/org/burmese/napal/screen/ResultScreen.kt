package org.burmese.napal.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import coil3.compose.AsyncImage
import org.burmese.napal.component.NapalHeader
import org.burmese.napal.component.NapalText
import org.burmese.napal.component.drawGradientBackground
import org.burmese.napal.domain.Card
import org.burmese.napal.domain.Prompt
import org.burmese.napal.viewmodel.ResultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    card: Card,
    onBack: () -> Unit,
    viewModel: ResultViewModel = viewModel { ResultViewModel() }
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val displayedImage = uiState.generatedImage ?: card.byteArray

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
                modifier = Modifier.zIndex(1f),
                title = "내 카드",
                onBack = onBack
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = 224.dp, height = 310.dp)
                    .graphicsLayer {
                        rotationX = rotation.value.x
                        rotationY = rotation.value.y
                        scaleX = scale.value
                        scaleY = scale.value
                        cameraDistance = 8 * density
                    }
                    .clip(RoundedCornerShape(24.dp))
                    .pointerInput(Unit) {
                        var currentScale = scale.value
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val pressed = event.changes.filter { it.pressed }

                                when (pressed.size) {
                                    2 -> {
                                        val zoom = event.calculateZoom()
                                        currentScale = (currentScale * zoom).coerceIn(1f, 2.5f)
                                        scope.launch { scale.snapTo(currentScale) }
                                    }
                                    1 -> {
                                        val pos = pressed.first().position
                                        val tiltY = (((pos.x / size.width).coerceIn(0f, 1f)) - 0.5f) * 45f
                                        val tiltX = -(((pos.y / size.height).coerceIn(0f, 1f)) - 0.5f) * 45f
                                        scope.launch { rotation.snapTo(Offset(tiltX, tiltY)) }
                                    }
                                    else -> {
                                        scope.launch {
                                            rotation.animateTo(
                                                Offset.Zero,
                                                tween(durationMillis = 1000, easing = EaseInOut)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = displayedImage,
                    contentDescription = "img_card",
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, end = 16.dp, bottom = 18.dp),
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
                        if (card.tag.isBlank()) return@FlowRow
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
                                    .height(20.dp),
                                contentAlignment = Alignment.Center
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
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 26.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPressed) {
                        Color(0xFF2FB3AA)
                    } else {
                        Color(0xFF46d6cd)
                    },
                ),
                shape = RoundedCornerShape(18.dp),
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

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x99000000))
                        .clickable(indication = null, interactionSource = null) { /** no-op */ },
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF46d6cd))
                }
            }

            uiState.error?.let { errorMessage ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 96.dp)
                        .background(Color(0xCC0C0D1A), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    NapalText(
                        text = errorMessage,
                        fontSize = 12.dp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFEEF0F8)
                    )
                }
            }
        }

        if (showBottomSheet) {
            val styleOptions = listOf(
                "유화" to Prompt.OilPainting(),
                "실사" to Prompt.Photorealistic(),
                "애니메이션" to Prompt.AnimeStyle(),
                "도트" to Prompt.PixelArt()
            )

            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NapalText(
                        text = "어떤 스타일로 그려드릴까요?",
                        fontSize = 16.dp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C0D1A)
                    )
                    styleOptions.forEach { (label, prompt) ->
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF46d6cd)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            onClick = {
                                val byteArray = card.byteArray
                                if (byteArray != null) {
                                    viewModel.generateImage(prompt, 0.5, byteArray)
                                }
                                scope.launch { bottomSheetState.hide() }
                                showBottomSheet = false
                            }
                        ) {
                            NapalText(
                                text = label,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.dp,
                                color = Color(0xFF072B2A)
                            )
                        }
                    }
                }
            }
        }
    }
}