package com.example.inrussian.data.client.models

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetResponse(
    val ok: Boolean, val error: String? = null, val reason: String? = null
)
