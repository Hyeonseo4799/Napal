package org.burmese.pomi.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        modifier = modifier.size(38.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) {
                Color(0xFF13252F)
            } else {
                Color(0x0DFFFFFF)
            },
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isPressed) {
                Color(0xFF1D4950)
            } else {
                Color(0x14FFFFFF)
            }
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp), // 기본 패딩 삭제 (텍스트 잘림 방지)
        interactionSource = interactionSource
    ) {
        PomiText(
            text = "‹",
            fontSize = 20.dp,
            color = Color(0xFFC8CBE0),
            fontWeight = FontWeight.Medium
        )
    }
}