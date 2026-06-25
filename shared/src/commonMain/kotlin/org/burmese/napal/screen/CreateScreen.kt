package org.burmese.napal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import kotlinx.coroutines.launch
import napal.shared.generated.resources.Res
import napal.shared.generated.resources.img_placeholder_card
import org.burmese.napal.component.NapalHeader
import org.burmese.napal.component.NapalText
import org.burmese.napal.component.NapalTextField
import org.burmese.napal.component.drawGradientBackground
import org.burmese.napal.domain.Card
import org.jetbrains.compose.resources.painterResource

@Composable
fun CreateScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    card: Card,
    onUpdateCard: (Card) -> Unit,
    goToResult: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val imagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = coroutineScope,
        onResult = { byteArrays ->
            onUpdateCard(card.copy(byteArray = byteArrays.firstOrNull()))
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawGradientBackground() }
            .then(modifier)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        NapalHeader(
            modifier = Modifier.zIndex(1f),
            title = "새 카드 만들기",
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, start = 24.dp, end = 24.dp, bottom = 96.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NapalText(
                modifier = Modifier.fillMaxSize(),
                text = "카드 이미지",
                fontSize = (12.5).dp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8A8DA6)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.size(width = 178.dp, height = 247.dp)) {
                if (card.byteArray != null) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(24.dp)),
                        model = card.byteArray,
                        contentDescription = "img_card",
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(Res.drawable.img_placeholder_card),
                        contentDescription = "img_placeholder_card"
                    )
                }
                Button(
                    modifier = Modifier
                        .size(width = 50.dp, height = 30.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-12).dp, y = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x990c0d1a)
                    ),
                    shape = RoundedCornerShape(99.dp),
                    contentPadding = PaddingValues(0.dp), // 기본 패딩 제거
                    onClick = { imagePicker.launch() }
                ) {
                    NapalText(
                        text = "변경",
                        fontSize = 12.dp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE9EBF6)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            NapalText(
                modifier = Modifier.fillMaxSize(),
                text = "카드 이름",
                fontSize = (12.5).dp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8A8DA6)
            )
            Spacer(modifier = Modifier.height(8.dp))
            NapalTextField(
                value = card.name,
                onValueChange = { onUpdateCard(card.copy(name = it)) },
                placeholder = "카드 이름을 입력해주세요"
            )
            Spacer(modifier = Modifier.height(16.dp))
            NapalText(
                modifier = Modifier.fillMaxSize(),
                text = "태그 (콤마로 구분)",
                fontSize = (12.5).dp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8A8DA6)
            )
            Spacer(modifier = Modifier.height(8.dp))
            NapalTextField(
                value = card.tag,
                onValueChange = { onUpdateCard(card.copy(tag = it)) },
                placeholder = "태그를 입력해주세요"
            )
            Spacer(modifier = Modifier.height(16.dp))
            NapalText(
                modifier = Modifier.fillMaxSize(),
                text = "한 줄 메시지",
                fontSize = (12.5).dp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8A8DA6)
            )
            Spacer(modifier = Modifier.height(8.dp))
            NapalTextField(
                value = card.message,
                onValueChange = { onUpdateCard(card.copy(message = it)) },
                placeholder = "한 줄 메시지를 입력해주세요"
            )
        }
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(top = 14.dp, bottom = 26.dp, start = 24.dp, end = 24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPressed) {
                    Color(0xFF2FB3AA)
                } else {
                    Color(0xFF46d6cd)
                }
            ),
            shape = RoundedCornerShape(18.dp),
            onClick = {
                if (card.byteArray == null) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("카드 이미지를 선택해주세요")
                    }
                } else {
                    goToResult.invoke()
                }
            }
        ) {
            NapalText(
                text = "✦ 카드 만들기",
                fontSize = 16.dp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF072B2A)
            )
        }
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState,
        )
    }
}