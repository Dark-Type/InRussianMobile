package com.example.inrussian.data.client.models

import kotlinx.serialization.Serializable

@Serializable
public final data class RecoveryEmailRequest(
    public final val email: String
)