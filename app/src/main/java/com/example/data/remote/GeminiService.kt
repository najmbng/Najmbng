package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @param:Json(name = "contents") val contents: List<ContentItem>,
    @param:Json(name = "generationConfig") val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class ContentItem(
    @param:Json(name = "parts") val parts: List<PartItem>
)

@JsonClass(generateAdapter = true)
data class PartItem(
    @param:Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @param:Json(name = "temperature") val temperature: Float? = 0.7f,
    @param:Json(name = "topP") val topP: Float? = 0.95f,
    @param:Json(name = "topK") val topK: Int? = 40,
    @param:Json(name = "responseModalities") val responseModalities: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @param:Json(name = "candidates") val candidates: List<CandidateItem>?
)

@JsonClass(generateAdapter = true)
data class CandidateItem(
    @param:Json(name = "content") val content: ContentItem?
)

interface GeminiApi {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    const val MODEL_PRO_COMPLEX = "gemini-3.1-pro-preview"
    const val MODEL_FLASH_GENERAL = "gemini-3.5-flash"
    const val MODEL_FLASH_LITE_FAST = "gemini-3.1-flash-lite-preview"
    const val MODEL_LYRIA_CLIP = "lyria-3-clip-preview"
    const val MODEL_LYRIA_PRO = "lyria-3-pro-preview"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }

    suspend fun requestPrompt(
        prompt: String,
        model: String = MODEL_FLASH_GENERAL,
        temperature: Float = 0.6f
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            throw IllegalStateException("API_KEY_UNSET")
        }

        val request = GeminiRequest(
            contents = listOf(
                ContentItem(
                    parts = listOf(PartItem(text = prompt))
                )
            ),
            generationConfig = GenerationConfig(
                temperature = temperature
            )
        )

        val response = api.generateContent(model, apiKey, request)
        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        text ?: throw IllegalStateException("Empty response from Gemini ($model)")
    }

    suspend fun generateMusic(
        prompt: String,
        model: String = MODEL_LYRIA_CLIP
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            throw IllegalStateException("API_KEY_UNSET")
        }

        val request = GeminiRequest(
            contents = listOf(
                ContentItem(
                    parts = listOf(PartItem(text = prompt))
                )
            ),
            generationConfig = GenerationConfig(
                responseModalities = listOf("AUDIO")
            )
        )

        val response = api.generateContent(model, apiKey, request)
        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Generated soundtrack metadata"
        text
    }
}
