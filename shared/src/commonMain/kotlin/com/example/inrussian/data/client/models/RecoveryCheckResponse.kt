package com.example.inrussian.data.client.models

import kotlinx.serialization.Serializable

@Serializable
data class RecoveryCheckResponse(
    val ok: Boolean,
    val reason: String? = null
)
