package org.burmese.napal.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import org.burmese.napal.domain.Prompt
import org.burmese.napal.domain.Prompt.Companion.NEGATIVE_PROMPT

object ApiClient {
    private const val BASE_URL = "https://api.cloudflare.com/client/v4/accounts/"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun generateImage(prompt: Prompt, strength: Double, image: String): ByteArray {
        return client.post("$BASE_URL${Secrets.CF_ACCOUNT_ID}/ai/run/@cf/runwayml/stable-diffusion-v1-5-img2img") {
            header(HttpHeaders.Authorization, "Bearer ${Secrets.CF_API_TOKEN}")
            contentType(ContentType.Application.Json)
            setBody(
                ImageToImageRequest(
                    prompt = prompt.text,
                    image_b64 = image,
                    negative_prompt = NEGATIVE_PROMPT,
                    width = 832,
                    height = 1156,
                    strength = strength,
                )
            )
        }.body()
    }
}

@Serializable
private data class ImageToImageRequest(
    val prompt: String,
    val image_b64: String,
    val negative_prompt: String,
    val width: Int,
    val height: Int,
    val strength: Double
)