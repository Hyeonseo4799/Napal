package org.burmese.pomi.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.burmese.pomi.domain.Prompt

object ApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash-image:generateContent"
    private const val TIMEOUT_MILLIS = 3 * 60 * 1000L

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = TIMEOUT_MILLIS
            requestTimeoutMillis = TIMEOUT_MILLIS
            connectTimeoutMillis = TIMEOUT_MILLIS
        }

    }

    suspend fun generateImage(prompt: Prompt, image: String): ApiResponse {
        return client.post(BASE_URL) {
            header("x-goog-api-key", Secrets.CF_API_TOKEN)
            contentType(ContentType.Application.Json)
            setBody(
                Request(
                    listOf(
                        Content(
                            listOf(
                                Part(
                                    inlineData = InlineData(
                                        mimeType = "image/jpeg",
                                        data = image
                                    )
                                ),
                                Part(
                                    text = prompt.text
                                )
                            )
                        )
                    )
                )
            )
        }.body()
    }
}

@Serializable
private data class Request(
    val contents: List<Content>
)

@Serializable
private data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    @SerialName("inline_data")
    val inlineData: InlineData? = null, // text랑 둘 중 하나만 들어감
    val text: String? = null // inlineData랑 둘 중 하나만 들어감
)

@Serializable
data class InlineData(
    @SerialName("mime_type")
    val mimeType: String,
    val data: String
)

@Serializable
data class ApiResponse(
    @SerialName("candidates")
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    @SerialName("content")
    val content: ContentResponse? = null
)

@Serializable
data class ContentResponse(
    @SerialName("parts")
    val parts: List<PartResponse>? = null
)

@Serializable
data class PartResponse(
    @SerialName("inlineData")
    val inlineData: InlineDataResponse? = null
)

@Serializable
data class InlineDataResponse(
    @SerialName("mimeType")
    val mimeType: String? = null,
    @SerialName("data")
    val data: String? = null
)