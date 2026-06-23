package org.burmese.napal.component

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform

fun DrawScope.drawGradientBackground() {
    drawRect(Color(0xFF0C0D1A))

    val cx = size.width * 0.50f
    val cy = size.height * 0.16f
    val rx = size.width * 1.20f
    val ry = size.height * 0.75f

    withTransform({
        // y 기준 원을 x 방향으로 늘려서 타원으로
        scale(scaleX = rx / ry, scaleY = 1f, pivot = Offset(cx, cy))
    }) {
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to Color(0xFF23264A),
                    0.64f to Color(0xFF0C0D1A)
                ),
                center = Offset(cx, cy),
                radius = ry
            ),
            radius = ry,
            center = Offset(cx, cy)
        )
    }
}