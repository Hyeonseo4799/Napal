package org.burmese.napal.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
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

    Box(
        modifier = Modifier
            .drawBehind { drawGradientBackground() }
            .fillMaxSize()
            .padding(horizontal = 24.dp)
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
                    .clip(RoundedCornerShape(24.dp))
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

                    ) {
//                        card.tag.split(",").map { it. }
                        Box(
                            modifier = Modifier.height(20.dp)
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = { showBottomSheet = true },
            ) {
                NapalText(
                    text = "✦ AI 페인팅",
                    fontWeight = FontWeight.Bold,
                    fontSize = (15.5).dp,
                    color = Color(0x072B2A)
                )
            }
        }
    }
}