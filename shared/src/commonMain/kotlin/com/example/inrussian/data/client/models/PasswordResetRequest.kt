package com.example.inrussian.data.client.models

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val email: String,
    val code: String,
    val newPassword: String
)
