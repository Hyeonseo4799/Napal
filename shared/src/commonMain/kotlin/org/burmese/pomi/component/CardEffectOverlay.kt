package org.burmese.pomi.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.burmese.pomi.domain.CardSpecialEffect
import kotlin.random.Random

private val CARD_WIDTH = 224.dp
private val CARD_HEIGHT = 310.dp
private val CARD_CORNER = 24.dp

private data class FloatingParticle(
    val emoji: String,
    val relX: Float,           // -0.5..0.5 relative to card center
    val peakHeightDp: Float,   // dp, how high it flies
    val driftXDp: Float,       // dp, horizontal drift
    val delay: Float           // 0..0.35 normalized time before launch
)

@Composable
fun CardEffectOverlay(
    modifier: Modifier = Modifier,
    effect: CardSpecialEffect,
    effectSpinY: Animatable<Float, AnimationVector1D>,
    effectScale: Animatable<Float, AnimationVector1D>,
    onFinished: () -> Unit
) {
    Box(
        modifier = modifier.clickable(indication = null, interactionSource = null) { }
    ) {
        when (effect) {
            is CardSpecialEffect.BorderGlow -> BorderGlowEffect(onFinished)
            is CardSpecialEffect.Pulse -> PulseEffect(effectScale, onFinished)
            is CardSpecialEffect.Spin -> SpinEffect(effectSpinY, onFinished)
            is CardSpecialEffect.LightBeam -> LightBeamEffect(onFinished)
            is CardSpecialEffect.FloatingEmojis -> FloatingEmojisEffect(onFinished)
        }
    }
}

// ── 1. 테두리 글로우 ────────────────────────────────────────────────

@Composable
private fun BoxScope.BorderGlowEffect(onFinished: () -> Unit) {
    val glowAlpha = remember { Animatable(0f) }
    val glowColors = listOf(Color(0xFF46D6CD), Color(0xFF8B5CF6), Color(0xFFFF6090))
    var colorIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(3) { i ->
            colorIndex = i
            glowAlpha.animateTo(1f, tween(280, easing = EaseInOut))
            glowAlpha.animateTo(0.05f, tween(280, easing = EaseInOut))
        }
        glowAlpha.animateTo(0f, tween(150))
        onFinished()
    }

    val glowPad = 28.dp
    Canvas(
        modifier = Modifier
            .align(Alignment.Center)
            .size(
                width = CARD_WIDTH + glowPad * 2,
                height = CARD_HEIGHT + glowPad * 2
            )
    ) {
        val pad = glowPad.toPx()
        val cardW = CARD_WIDTH.toPx()
        val cardH = CARD_HEIGHT.toPx()
        val corner = CARD_CORNER.toPx()
        val alpha = glowAlpha.value
        val color = glowColors[colorIndex]

        for (i in 0 until 6) {
            val expand = (i * 3.5f).dp.toPx()
            val strokeAlpha = alpha * (1f - i * 0.16f).coerceAtLeast(0f)
            drawRoundRect(
                color = color.copy(alpha = strokeAlpha),
                topLeft = Offset(pad - expand, pad - expand),
                size = Size(cardW + expand * 2, cardH + expand * 2),
                cornerRadius = CornerRadius(corner + expand, corner + expand),
                style = Stroke(width = (3.5f - i * 0.4f).coerceAtLeast(0.8f).dp.toPx())
            )
        }
    }
}

// ── 2. 펄스 (스케일) ────────────────────────────────────────────────

@Composable
private fun PulseEffect(
    effectScale: Animatable<Float, AnimationVector1D>,
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        effectScale.snapTo(1f)
        effectScale.animateTo(1.14f, tween(300, easing = EaseInOut))
        effectScale.animateTo(0.91f, tween(240, easing = EaseInOut))
        effectScale.animateTo(1.09f, tween(230, easing = EaseInOut))
        effectScale.animateTo(0.96f, tween(220, easing = EaseInOut))
        effectScale.animateTo(1.03f, tween(200, easing = EaseInOut))
        effectScale.animateTo(1f, tween(210, easing = EaseInOut))
        onFinished()
    }
}

// ── 3. 제자리 회전 ──────────────────────────────────────────────────

@Composable
private fun SpinEffect(
    effectSpinY: Animatable<Float, AnimationVector1D>,
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        effectSpinY.snapTo(0f)
        effectSpinY.animateTo(360f, tween(1800, easing = LinearEasing))
        effectSpinY.snapTo(0f)
        onFinished()
    }
}

// ── 4. 대각선 빛줄기 ────────────────────────────────────────────────

@Composable
private fun BoxScope.LightBeamEffect(onFinished: () -> Unit) {
    val beamProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        repeat(3) {
            beamProgress.snapTo(0f)
            beamProgress.animateTo(1f, tween(480, easing = LinearEasing))
            delay(80)
        }
        onFinished()
    }

    Canvas(
        modifier = Modifier
            .align(Alignment.Center)
            .size(width = CARD_WIDTH, height = CARD_HEIGHT)
            .clip(RoundedCornerShape(CARD_CORNER))
    ) {
        val t = beamProgress.value
        val beamW = size.width * 0.28f
        val beamCenterX = t * (size.width + beamW * 2) - beamW

        rotate(degrees = -35f) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0x40FFFFFF),
                        Color(0x90FFFFFF),
                        Color(0x40FFFFFF),
                        Color.Transparent
                    ),
                    startX = beamCenterX - beamW / 2f,
                    endX = beamCenterX + beamW / 2f
                ),
                topLeft = Offset(0f, -size.height),
                size = Size(size.width, size.height * 3f)
            )
        }
    }
}

// ── 5. 반짝이 이모지 ────────────────────────────────────────────────

@Composable
private fun BoxScope.FloatingEmojisEffect(onFinished: () -> Unit) {
    val textMeasurer = rememberTextMeasurer()

    val particles = remember {
        val emojis = listOf("✨", "⭐", "🌟", "💫", "✦", "★", "✧")
        (0 until 7).map { i ->
            FloatingParticle(
                emoji = emojis[i],
                relX = (i.toFloat() / 6f) - 0.5f + (Random.nextFloat() - 0.5f) * 0.08f,
                peakHeightDp = 110f + Random.nextFloat() * 110f,
                driftXDp = (Random.nextFloat() - 0.5f) * 36f,
                delay = Random.nextFloat() * 0.35f
            )
        }
    }

    val layouts = remember {
        particles.map { textMeasurer.measure(it.emoji, TextStyle(fontSize = 22.sp)) }
    }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(2000, easing = LinearEasing))
        onFinished()
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val t = progress.value
        val cardW = CARD_WIDTH.toPx()
        val cardH = CARD_HEIGHT.toPx()
        val cardCenterX = size.width / 2f
        val cardCenterY = size.height / 2f
        val cardBottomY = cardCenterY + cardH / 2f

        particles.forEachIndexed { i, p ->
            val pt = ((t - p.delay) / (1f - p.delay)).coerceIn(0f, 1f)
            if (pt <= 0f) return@forEachIndexed

            val peakPx = p.peakHeightDp.dp.toPx()
            val driftPx = p.driftXDp.dp.toPx()
            val startX = cardCenterX + p.relX * cardW
            val x = startX + driftPx * pt
            val y = cardBottomY - peakPx * 4f * pt * (1f - pt)

            val alpha = when {
                pt < 0.12f -> pt / 0.12f
                pt > 0.72f -> (1f - pt) / 0.28f
                else -> 1f
            }

            val layout = layouts[i]
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(x - layout.size.width / 2f, y - layout.size.height / 2f),
                alpha = alpha
            )
        }
    }
}
