package org.burmese.napal.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import napal.shared.generated.resources.Res
import napal.shared.generated.resources.pretendard_bold
import napal.shared.generated.resources.pretendard_extrabold
import napal.shared.generated.resources.pretendard_medium
import napal.shared.generated.resources.pretendard_semibold
import org.jetbrains.compose.resources.Font

val Pretendard: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.pretendard_extrabold, FontWeight.ExtraBold),
        Font(Res.font.pretendard_bold, FontWeight.Bold),
        Font(Res.font.pretendard_semibold, FontWeight.SemiBold),
        Font(Res.font.pretendard_medium, FontWeight.Medium)
    )