package org.burmese.napal.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import org.burmese.napal.component.AiPaintingBottomSheet
import org.burmese.napal.component.NapalHeader
import org.burmese.napal.component.NapalText
import org.burmese.napal.component.drawGradientBackground
import org.burmese.napal.domain.Card
import org.burmese.napal.platform.saveImageToGallery
import org.burmese.napal.viewmodel.ResultViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    card: Card,
    onBack: () -> Unit,
    viewModel: ResultViewModel = viewModel { ResultViewModel() }
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var isDarkText by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val cardGraphicsLayer = rememberGraphicsLayer()
    val platformContext = LocalPlatformContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val displayedImage = uiState.generatedImage ?: card.byteArray

    var isSaving by remember { mutableStateOf(false) }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(saveMessage) {
        if (saveMessage != null) {
            delay(2000)
            saveMessage = null
        }
    }

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
                onBack = onBack,
                trailing = {
                    TextColorToggleButton(
                        isActive = isDarkText,
                        onClick = { isDarkText = !isDarkText }
                    )
                }
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
                    .drawWithContent {
                        cardGraphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        cardGraphicsLayer.clip = true
                        cardGraphicsLayer.setRoundRectOutline(cornerRadius = 24.dp.toPx())
                        drawLayer(cardGraphicsLayer)
                    }
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = displayedImage,
                    contentDescription = "img_card",
                    contentScale = ContentScale.Crop,
                )
                val cardNameColor = if (isDarkText) Color(0xFF14151F) else Color(0xFFEEF0F8)
                val cardMessageColor = if (isDarkText) Color(0xFF2E3142) else Color(0xFFC8CBE0)
                val cardTagTextColor = if (isDarkText) Color(0xFF0F3F3B) else Color(0xFFA9E3DD)

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
                        color = cardNameColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    NapalText(
                        text = card.message,
                        fontSize = 12.dp,
                        fontWeight = FontWeight.Medium,
                        color = cardMessageColor,
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
                                    color = cardTagTextColor,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
            }
            val paintInteractionSource = remember { MutableInteractionSource() }
            val paintIsPressed by paintInteractionSource.collectIsPressedAsState()

            val shareInteractionSource = remember { MutableInteractionSource() }
            val shareIsPressed by paintInteractionSource.collectIsPressedAsState()
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 26.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (paintIsPressed) {
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
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (shareIsPressed) {
                            Color(0xFF13252F)
                        } else {
                            Color(0x0DFFFFFF)
                        },
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (shareIsPressed) {
                            Color(0xFF1D4950)
                        } else {
                            Color(0x1FFFFFFF)
                        }
                    ),
                    shape = CircleShape,
                    enabled = !isSaving,
                    onClick = {
                        scope.launch {
                            isSaving = true
                            val bitmap = cardGraphicsLayer.toImageBitmap()
                            val fileName = "napal_${card.name.ifBlank { "card" }}_${Random.nextInt(100000)}"
                            val saved = saveImageToGallery(platformContext, bitmap, fileName)
                            saveMessage = if (saved) "갤러리에 저장했어요" else "저장에 실패했어요"
                            isSaving = false
                        }
                    },
                    interactionSource = shareInteractionSource
                ) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        val arrowColor = Color(0xFFE9EBF6)
                        val strokeWidth = 1.8.dp.toPx()
                        val centerX = size.width / 2f
                        val stemBottomY = size.height * 0.65f
                        val arrowSpread = size.width * 0.5f

                        drawLine(
                            color = arrowColor,
                            start = Offset(centerX, 0f),
                            end = Offset(centerX, stemBottomY),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = arrowColor,
                            start = Offset(centerX - arrowSpread / 2f, stemBottomY - arrowSpread / 2f),
                            end = Offset(centerX, size.height),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = arrowColor,
                            start = Offset(centerX + arrowSpread / 2f, stemBottomY - arrowSpread / 2f),
                            end = Offset(centerX, size.height),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
            // 에러 처리 추후 변경
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

            saveMessage?.let { message ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 96.dp)
                        .background(Color(0xCC0C0D1A), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    NapalText(
                        text = message,
                        fontSize = 12.dp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFEEF0F8)
                    )
                }
            }
        }

        if (showBottomSheet) {
            AiPaintingBottomSheet(
                onDismiss = { showBottomSheet = false },
                onGenerate = { prompt ->
                    showBottomSheet = false
                    val byteArray = card.byteArray
                    byteArray?.let { viewModel.generateImage(prompt = prompt, byteArray = byteArray) }
                        ?: run { scope.launch { snackbarHostState.showSnackbar("원본 이미지를 찾을 수 없어요") } }
                }
            )
        }

        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .zIndex(2f) // 헤더 보다 상위
                    .fillMaxSize()
                    .background(Color(0x99000000))
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) { /** no-op */ },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)
            ) {
                NapalText(
                    text = "AI 페인팅 기능은 10초 이상 걸릴 수 있어요\n장시간 대기해도 안된다면 재접속 해주세요.",
                    fontSize = 12.dp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFEEF0F8),
                )
                CircularProgressIndicator(color = Color(0xFF46d6cd))
            }
        }
    }
}

@Composable
private fun TextColorToggleButton(
    isActive: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isActive -> Color(0xFF46D6CD)
                isPressed -> Color(0xFF13252F)
                else -> Color(0x0DFFFFFF)
            },
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                isActive -> Color(0xFF46D6CD)
                isPressed -> Color(0xFF1D4950)
                else -> Color(0x14FFFFFF)
            }
        ),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp), // 기본 패딩 삭제 (텍스트 잘림 방지)
        interactionSource = interactionSource
    ) {
        NapalText(
            text = "T",
            fontSize = 14.dp,
            color = if (isActive) Color(0xFF072B2A) else Color(0xFFC8CBE0),
            fontWeight = FontWeight.Bold
        )
    }
}