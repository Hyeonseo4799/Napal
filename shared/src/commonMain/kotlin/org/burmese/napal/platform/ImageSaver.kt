package org.burmese.napal.platform

import androidx.compose.ui.graphics.ImageBitmap
import coil3.PlatformContext

expect suspend fun saveImageToGallery(
    context: PlatformContext,
    imageBitmap: ImageBitmap,
    fileName: String
): Boolean