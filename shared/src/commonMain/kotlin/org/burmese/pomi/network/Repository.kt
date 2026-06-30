package org.burmese.pomi.network

import org.burmese.pomi.domain.Prompt
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class Repository {
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun generateImage(prompt: Prompt, byteArray: ByteArray): ByteArray? {
        val image = Base64.encode(byteArray)
        val response = ApiClient.generateImage(prompt, image)
        val content = response.candidates?.first()?.content ?: return null
        val imageBase64 = content.parts?.mapNotNull { it.inlineData?.data }?.first() ?: return null
        return Base64.decode(imageBase64)
    }
}