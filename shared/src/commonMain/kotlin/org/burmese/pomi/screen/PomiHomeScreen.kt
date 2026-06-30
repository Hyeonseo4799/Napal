package org.burmese.pomi.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────────
//  POMI 홈  ·  밤하늘 퍼플
//  New Pomi.dc.html 3번(메인·홈) 기준
//  상단 P O M I · 중앙 글래스 카드 + 크로스헤어 · 하단 바텀시트 + 안드로이드 내비
// ─────────────────────────────────────────────────────────────────

// 팔레트
private val Base       = Color(0xFF0A0718)
private val NightViolet = Color(0xFF1F1748)
private val Violet     = Color(0x99B18CF2)
private val Periwinkle = Color(0xFF6A7FF0)
private val Orchid     = Color(0xFFC98CE0)
private val Ink        = Color(0xFFEFEAFF)
private val Star       = Color(0xFFEEE6FF)

private val DisplayFont = FontFamily.Serif   // Cinzel 대체 (res/font 로 교체 권장)

// CTA / EXP 그라디언트
private val CtaBrush = Brush.linearGradient(listOf(Color(0xFF6A4CC8), Violet, Color(0xFF8A6BE0)))
private val ExpBrush = Brush.linearGradient(listOf(Color(0xFF5B3FB0), Violet))

@Composable
fun PomiHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawHomeBackground() }
    ) {
        // ── 상단(상태바 + 워드마크) + 카드 영역 ─────────────────
        Column(Modifier.fillMaxSize()) {
            PomiStatusBar()

            Box(Modifier.weight(1f).fillMaxWidth()) {
                // P O M I 워드마크
                Text(
                    text = "POMI",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 38.dp),
                    color = Color(0xFFE6DAFF).copy(alpha = 0.9f),
                    fontFamily = DisplayFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 8.sp // ≒ 0.6em
                )
                // 글래스 카드 + 크로스헤어
                PomiHomeCard(Modifier.align(Alignment.Center))
            }

            // 바텀시트 자리 확보
            Spacer(Modifier.height(248.dp))
        }

        // ── 바텀시트 ─────────────────────────────────────────
        PomiBottomSheet(Modifier.align(Alignment.BottomCenter))

        // ── 안드로이드 시스템 내비게이션 바 ──────────────────
        PomiSystemNavBar(Modifier.align(Alignment.BottomCenter))
    }
}

// ─────────────────────────────────────────────────────────────────
//  상태바
// ─────────────────────────────────────────────────────────────────
@Composable
private fun PomiStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .padding(horizontal = 26.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("9:41", color = Color(0xFFCFC4F2), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            // 신호 막대
            Canvas(Modifier.size(width = 15.dp, height = 11.dp)) {
                val bar = 3.dp.toPx(); val gap = 1.dp.toPx()
                val hs = listOf(0.45f, 0.64f, 0.82f, 1f)
                hs.forEachIndexed { i, f ->
                    val bh = size.height * f
                    drawRect(Color(0xFFCFC4F2), Offset(i * (bar + gap), size.height - bh), Size(bar, bh))
                }
            }
            // 배터리
            Canvas(Modifier.size(width = 22.dp, height = 11.dp)) {
                val w = size.width - 2.dp.toPx()
                drawRoundRect(Color(0xFFCFC4F2), size = Size(w, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
                    style = Stroke(1.dp.toPx()))
                val pad = 1.5.dp.toPx()
                drawRoundRect(Color(0xFFCFC4F2), topLeft = Offset(pad, pad),
                    size = Size((w - 2 * pad) * 0.72f, size.height - 2 * pad),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx()))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
//  글래스 카드 + 크로스헤어 프레임 (220 x 300)
// ─────────────────────────────────────────────────────────────────
@Composable
private fun PomiHomeCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(width = 220.dp, height = 300.dp),
        contentAlignment = Alignment.Center
    ) {
        // 카드 (164 x 228)
        Box(
            modifier = Modifier
                .size(width = 164.dp, height = 228.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Violet.copy(alpha = 0.16f),
                            Periwinkle.copy(alpha = 0.10f),
                            Orchid.copy(alpha = 0.08f)
                        )
                    )
                )
                .border(1.dp, Violet.copy(alpha = 0.5f), RoundedCornerShape(13.dp))
        ) {
            // 상단 하이라이트 (42%)
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.42f)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.08f), Color.Transparent)
                        )
                    )
            )
        }

        // 크로스헤어 코너
        Canvas(Modifier.matchParentSize()) {
            val armLength = 54.dp.toPx()
            val strokeWidth = 1.5.dp.toPx()
            val color = Violet.copy(alpha = 0.6f)

            fun drawCorner(corner: Offset, horizontalLength: Float, verticalLength: Float) {
                val horizontalTip = Offset(corner.x + horizontalLength, corner.y)
                val verticalTip = Offset(corner.x, corner.y + verticalLength)
                drawLine(Brush.linearGradient(listOf(color, Color.Transparent), corner, horizontalTip), corner, horizontalTip, strokeWidth)
                drawLine(Brush.linearGradient(listOf(color, Color.Transparent), corner, verticalTip), corner, verticalTip, strokeWidth)
            }

            drawCorner(Offset(0f, 0f),          +armLength, +armLength)  // top-left
            drawCorner(Offset(size.width, 0f),   -armLength, +armLength)  // top-right
            drawCorner(Offset(0f, size.height),  +armLength, -armLength)  // bottom-left
            drawCorner(Offset(size.width, size.height), -armLength, -armLength)  // bottom-right
        }
        // 코너 ✦
        val starColor = Color(0xFFDCCDFF)
        Text("✦", Modifier.align(Alignment.TopStart), color = starColor, fontSize = 10.sp)
        Text("✦", Modifier.align(Alignment.TopEnd), color = starColor, fontSize = 10.sp)
        Text("✦", Modifier.align(Alignment.BottomStart), color = starColor, fontSize = 10.sp)
        Text("✦", Modifier.align(Alignment.BottomEnd), color = starColor, fontSize = 10.sp)
    }
}

// ─────────────────────────────────────────────────────────────────
//  바텀시트
// ─────────────────────────────────────────────────────────────────
@Composable
private fun PomiBottomSheet(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(248.dp)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Brush.verticalGradient(listOf(Color(0xB8281C52), Color(0xEB120C26))))
            .drawBehind {
                drawLine(Violet.copy(alpha = 0.28f), Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
            }
            .padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 50.dp)
    ) {
        // 핸들
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp).height(4.dp)
                .clip(CircleShape)
                .background(Violet.copy(alpha = 0.42f))
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically)
        ) {
            // Lv + EXP
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Lv.7", color = Color(0xFFE6DAFF), fontFamily = DisplayFont,
                        fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("다음 레벨까지 38%", color = Color(0xFFCEBCFF).copy(alpha = 0.6f), fontSize = 11.sp)
                }
                Spacer(Modifier.height(7.dp))
                Box(
                    Modifier.fillMaxWidth().height(6.dp).clip(CircleShape)
                        .background(Violet.copy(alpha = 0.14f))
                ) {
                    Box(
                        Modifier.fillMaxWidth(0.62f).fillMaxHeight()
                            .clip(CircleShape).background(ExpBrush)
                    )
                }
            }

            // CTA
            Box(
                Modifier.fillMaxWidth().height(54.dp)
                    .clip(RoundedCornerShape(16.dp)).background(CtaBrush),
                contentAlignment = Alignment.Center
            ) {
                Text("✦  카드 만들기", color = Color(0xFF170A32),
                    fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            // 내비 텍스트
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("☾ 오늘의 운세", color = Color(0xFFD6C8FF).copy(alpha = 0.84f),
                    fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Box(Modifier.padding(horizontal = 12.dp).width(1.dp).height(14.dp)
                    .background(Violet.copy(alpha = 0.3f)))
                Text("▤ 내 앨범", color = Color(0xFFD6C8FF).copy(alpha = 0.84f),
                    fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
//  안드로이드 시스템 내비게이션 바
// ─────────────────────────────────────────────────────────────────
@Composable
private fun PomiSystemNavBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0x8C0A0718)),
        horizontalArrangement = Arrangement.spacedBy(78.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val c = Color(0xFFECE6FF).copy(alpha = 0.78f)
        // 뒤로 (◁)
        Canvas(Modifier.size(15.dp)) {
            val sw = 2.dp.toPx()
            drawLine(c, Offset(size.width, 0f), Offset(0f, size.height / 2f), sw, cap = StrokeCap.Round)
            drawLine(c, Offset(0f, size.height / 2f), Offset(size.width, size.height), sw, cap = StrokeCap.Round)
        }
        // 홈 (○)
        Canvas(Modifier.size(15.dp)) {
            drawCircle(c, radius = size.minDimension / 2f - 1.dp.toPx(), style = Stroke(2.dp.toPx()))
        }
        // 최근 (▢)
        Canvas(Modifier.size(14.dp)) {
            drawRoundRect(c, style = Stroke(2.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()))
        }
    }
}

// ─────────────────────────────────────────────────────────────────
//  배경: 단색 + 원형 그라데이션 + 글로우 블롭 (+ 옅은 별)
// ─────────────────────────────────────────────────────────────────
private fun DrawScope.drawHomeBackground() {
    drawRect(Base)
    drawRect(
        Brush.radialGradient(
            colorStops = arrayOf(0f to NightViolet, 0.7f to Color.Transparent),
            center = Offset(size.width * 0.5f, size.height * 0.26f),
            radius = size.width * 1.25f
        )
    )
    // 글로우 블롭 2개
    drawRect(
        Brush.radialGradient(
            colorStops = arrayOf(0f to Violet.copy(alpha = 0.16f), 1f to Color.Transparent),
            center = Offset(size.width * 0.20f, size.height * 0.28f),
            radius = size.width * 0.34f
        )
    )
    drawRect(
        Brush.radialGradient(
            colorStops = arrayOf(0f to Periwinkle.copy(alpha = 0.12f), 1f to Color.Transparent),
            center = Offset(size.width * 0.62f, size.height * 0.30f),
            radius = size.width * 0.32f
        )
    )
    // 옅은 별 (불필요하면 삭제)
    val px = 1.dp.toPx()
    drawCircle(Star.copy(alpha = 0.40f), 1.5f * px, Offset(size.width * 0.18f, size.height * 0.12f))
    drawCircle(Star.copy(alpha = 0.34f), 1.0f * px, Offset(size.width * 0.74f, size.height * 0.09f))
    drawCircle(Star.copy(alpha = 0.28f), 1.0f * px, Offset(size.width * 0.40f, size.height * 0.18f))
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
private fun PomiHomePreview() {
    PomiHomeScreen()
}
