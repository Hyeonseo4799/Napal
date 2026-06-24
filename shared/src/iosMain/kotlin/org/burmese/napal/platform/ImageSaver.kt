package org.burmese.napal.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import coil3.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Photos.PHAssetCreationRequest
import platform.Photos.PHAssetResourceType
import platform.Photos.PHPhotoLibrary

actual suspend fun saveImageToGallery(
    context: PlatformContext,
    imageBitmap: ImageBitmap,
    fileName: String
): Boolean {
    val skiaImage = Image.makeFromBitmap(imageBitmap.asSkiaBitmap())
    val pngBytes = skiaImage.encodeToData(EncodedImageFormat.PNG)?.bytes ?: return false
    val nsData = pngBytes.toNSData()

    return suspendCancellableCoroutine { continuation ->
        PHPhotoLibrary.sharedPhotoLibrary().performChanges({
            val request = PHAssetCreationRequest.creationRequestForAsset()
            request.addResourceWithType(PHAssetResourceType.PHAssetResourceTypePhoto, nsData, null)
        }) { success, _ ->
            continuation.resume(success)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}