package com.honeypot.app.ai

import android.util.Log
import com.honeypot.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AzureOpenAIRepository {
    private val tag = "AzureOpenAIRepository"

    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val apiKey = BuildConfig.AZURE_OPENAI_KEY
        val authInterceptor = Interceptor { chain ->
            val req = chain.request().newBuilder()
                .addHeader("api-key", apiKey)
                .build()
            chain.proceed(req)
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.AZURE_OPENAI_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val service by lazy { retrofit.create(AzureOpenAIService::class.java) }

    suspend fun chat(messages: List<ChatMessage>, maxTokens: Int? = 1024): ChatResponse? {
        val deployment = BuildConfig.AZURE_OPENAI_DEPLOYMENT
        val url = "${BuildConfig.AZURE_OPENAI_ENDPOINT}openai/deployments/$deployment/chat/completions?api-version=2024-12-01-preview"
        try {
            val req = ChatRequest(messages = messages, max_tokens = maxTokens)
            val resp = service.createChat(url, req)
            if (resp.isSuccessful) {
                return resp.body()
            } else {
                Log.e(tag, "chat error: ${resp.code()} ${resp.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e(tag, "chat exception", e)
        }
        return null
    }
}
