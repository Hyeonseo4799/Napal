package org.burmese.pomi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.burmese.pomi.domain.Prompt

private val SheetBg1 = Color(0xFF191B32)
private val SheetBg2 = Color(0xFF111224)
private val SheetBorderTop = Color(0x1AFFFFFF)
private val TextPrimary = Color(0xFFEEF0F8)
private val TextSecondary = Color(0xFF8A8DA6)
private val AccentCyan = Color(0xFF46D6CD)
private val AccentCyanBg = Color(0x1F46D6CD)
private val AccentCyanDark = Color(0xFF072B2A)
private val UnselBg = Color(0x0AFFFFFF)
private val UnselBorder = Color(0x17FFFFFF)
private val HandleColor = Color(0x2EFFFFFF)
private val CloseButtonBg = Color(0x0FFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPaintingBottomSheet(
    onDismiss: () -> Unit,
    onGenerate: (style: Prompt) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedStyle by remember { mutableStateOf<Prompt>(Prompt.AnimeStyle()) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        contentColor = TextPrimary,
        dragHandle = null,
        shape = sheetShape,
        scrimColor = Color(0x99060E0E),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(listOf(SheetBg1, SheetBg2)),
                    shape = sheetShape,
                )
                .border(width = 1.dp, color = SheetBorderTop, shape = sheetShape)
                .padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 26.dp)
                .navigationBarsPadding(),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 42.dp, height = 5.dp)
                    .clip(CircleShape)
                    .background(HandleColor)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PomiText("✦", color = AccentCyan, fontSize = 20.dp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.width(8.dp))
                        PomiText("AI 페인팅 (Beta)", color = TextPrimary, fontSize = 20.dp, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.height(6.dp))
                    PomiText(
                        text = "원본을 새로운 화풍으로 다시 그려요",
                        color = TextSecondary,
                        fontSize = 13.dp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CloseButtonBg)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismiss,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    PomiText("✕", color = Color(0xFFC8CBE0), fontSize = 15.dp)
                }
            }
            PomiText(
                text = "출력 타입",
                color = TextSecondary,
                fontSize = 13.dp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 20.dp, bottom = 11.dp),
            )
            val styleItems = listOf(
                Triple(Prompt.Photorealistic(), "실사화", SwatchKind.BlueNavy),
                Triple(Prompt.AnimeStyle(), "애니화", SwatchKind.PinkPurple),
                Triple(Prompt.OilPainting(), "유화", SwatchKind.OrangeYellow),
                Triple(Prompt.Doodle(), "낙서", SwatchKind.Pixel),
            )
            styleItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(11.dp),
                ) {
                    rowItems.forEach { (style, label, swatch) ->
                        StyleCard(
                            label = label,
                            swatch = swatch,
                            selected = selectedStyle == style,
                            onClick = { selectedStyle = style },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                Spacer(Modifier.height(11.dp))
            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { onGenerate(selectedStyle) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            ) {
                PomiText("✦  생성하기", fontSize = 16.dp, fontWeight = FontWeight.Bold, color = AccentCyanDark)
            }
        }
    }
}

private enum class SwatchKind { BlueNavy, PinkPurple, OrangeYellow, Pixel }

@Composable
private fun StyleCard(
    label: String,
    swatch: SwatchKind,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) AccentCyanBg else UnselBg)
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) AccentCyan else UnselBorder,
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 15.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .swatchBackground(swatch),
            )
            Spacer(Modifier.width(12.dp))
            PomiText(label, color = TextPrimary, fontSize = (14.5).dp, fontWeight = FontWeight.SemiBold)
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(AccentCyan)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center,
            ) {
                PomiText("✓", color = AccentCyanDark, fontSize = 12.dp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

private fun Modifier.swatchBackground(kind: SwatchKind): Modifier = when (kind) {
    SwatchKind.BlueNavy -> background(
        Brush.linearGradient(
            colors = listOf(Color(0xFF5A6B8C), Color(0xFF2D3552)),
            start = Offset(0f, 0f), end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
    )

    SwatchKind.PinkPurple -> background(
        Brush.linearGradient(
            colors = listOf(Color(0xFFE98BD0), Color(0xFF7C6CF0)),
            start = Offset(0f, 0f), end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
    )

    SwatchKind.OrangeYellow -> background(
        Brush.linearGradient(
            colors = listOf(Color(0xFFF0C46A), Color(0xFFD97A4E)),
            start = Offset(0f, 0f), end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
        )
    )

    SwatchKind.Pixel -> drawBehind { drawCheckerboard() }
}

private fun DrawScope.drawCheckerboard() {
    val cell = 12.dp.toPx()
    val cyan = Color(0xFF46D6CD)
    val navy = Color(0xFF2D3552)
    var row = 0
    var y = 0f
    while (y < size.height) {
        var col = 0
        var x = 0f
        while (x < size.width) {
            drawRect(
                color = if ((row + col) % 2 == 0) cyan else navy,
                topLeft = Offset(x, y),
                size = Size(minOf(cell, size.width - x), minOf(cell, size.height - y)),
            )
            x += cell; col++
        }
        y += cell; row++
    }
}