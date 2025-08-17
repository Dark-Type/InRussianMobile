package com.example.inrussian.models

import kotlinx.serialization.Serializable

@Serializable
enum class ErrorKey {
    INVALID_EMAIL,
    INVALID_PASSWORD,
    INVALID_CONFIRM_PASSWORD
}