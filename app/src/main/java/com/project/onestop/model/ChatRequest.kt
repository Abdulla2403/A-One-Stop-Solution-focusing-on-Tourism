package com.project.onestop.model

data class ChatRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)