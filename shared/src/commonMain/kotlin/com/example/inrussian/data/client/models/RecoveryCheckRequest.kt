package com.example.inrussian.data.client.models

import kotlinx.serialization.Serializable

@Serializable
public final data class RecoveryCheckRequest(
    public final val email: String,
    public final val code: String
)