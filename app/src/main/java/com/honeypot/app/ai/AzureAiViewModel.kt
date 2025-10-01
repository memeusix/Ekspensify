package com.honeypot.app.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DEFAULT_SYSTEM_PROMPT = "You are HoneyPot assistant."

data class ChatUiState(
    val conversation: List<ChatMessage> = emptyList(),
    val result: ChatResult = ChatResult.Idle
)

sealed interface ChatResult {
    data object Idle : ChatResult
    data object Loading : ChatResult
    data class Success(val message: ChatMessage) : ChatResult
    data class Error(val message: String) : ChatResult
}

@HiltViewModel
class AzureAiViewModel @Inject constructor(
    private val repository: AzureOpenAIRepository
) : ViewModel() {

    private val systemMessage = ChatMessage(role = "system", content = DEFAULT_SYSTEM_PROMPT)

    private val _chatState = MutableStateFlow(ChatUiState())
    val chatState: StateFlow<ChatUiState> = _chatState.asStateFlow()

    fun sendPrompt(prompt: String) {
        val sanitizedPrompt = prompt.trim()
        if (sanitizedPrompt.isEmpty()) return

        val userMessage = ChatMessage(role = "user", content = sanitizedPrompt)
        _chatState.update { current ->
            current.copy(
                conversation = current.conversation + userMessage,
                result = ChatResult.Loading
            )
        }

        viewModelScope.launch {
            val conversationWithSystem = buildList {
                add(systemMessage)
                addAll(_chatState.value.conversation)
            }

            try {
                val response = repository.chat(conversationWithSystem, maxTokens = 1024)
                val assistantMessage = response?.choices?.firstOrNull()?.message

                val normalizedAssistant = when {
                    assistantMessage == null -> {
                        emitError("No response from assistant.")
                        return@launch
                    }

                    assistantMessage.content.isBlank() -> {
                        emitError("No response from assistant.")
                        return@launch
                    }

                    else -> assistantMessage.copy(role = "assistant")
                }
                _chatState.update { current ->
                    current.copy(
                        conversation = current.conversation + normalizedAssistant,
                        result = ChatResult.Success(normalizedAssistant)
                    )
                }
            } catch (throwable: Throwable) {
                emitError(throwable.localizedMessage ?: "Something went wrong while contacting AI.")
            }
        }
    }

    private fun emitError(message: String) {
        _chatState.update { current ->
            current.copy(result = ChatResult.Error(message))
        }
    }
}
