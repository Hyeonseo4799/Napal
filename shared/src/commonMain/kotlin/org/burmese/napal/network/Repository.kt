package org.burmese.napal.network

import org.burmese.napal.domain.Prompt
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class Repository {
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun generateImage(prompt: Prompt, strength: Double, byteArray: ByteArray): ByteArray {
        val image = Base64.encode(byteArray)
        return ApiClient.generateImage(prompt, strength, image)
    }
}