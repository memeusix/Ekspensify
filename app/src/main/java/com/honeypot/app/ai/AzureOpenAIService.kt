package com.honeypot.app.ai

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

data class ChatMessage(val role: String, val content: String)
data class ChatRequest(val messages: List<ChatMessage>, val max_tokens: Int? = null, val model: String? = null)

data class ChatChoice(val index: Int, val message: ChatMessage)
data class ChatUsage(val prompt_tokens: Int?, val completion_tokens: Int?, val total_tokens: Int?)
data class ChatResponse(val id: String?, val choices: List<ChatChoice>?, val usage: ChatUsage?)

interface AzureOpenAIService {
    @Headers("Content-Type: application/json")
    @POST
    suspend fun createChat(@Url url: String, @Body body: ChatRequest): Response<ChatResponse>
}
