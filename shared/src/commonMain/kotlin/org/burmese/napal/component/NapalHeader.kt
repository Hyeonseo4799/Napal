package org.burmese.napal.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NapalHeader(
    modifier: Modifier = Modifier,
    title: String,
    onBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
    ) {
        BackButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp),
            onClick = onBack
        )
        NapalText(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            color = Color(0xFFEEF0F8),
            fontWeight = FontWeight.Bold,
            fontSize = 16.dp
        )
    }
}