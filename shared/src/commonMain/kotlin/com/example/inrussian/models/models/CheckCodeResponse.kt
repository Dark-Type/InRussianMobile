package com.example.inrussian.models.models

import io.ktor.websocket.CloseReason

data class CheckCodeResponse(
    val ok : Boolean,
    val reason: String
)
