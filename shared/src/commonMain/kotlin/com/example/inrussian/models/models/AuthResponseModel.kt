package com.example.inrussian.models.models

data class AuthResponseModel(
    val success: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val user: UserModel,
    val message: String,
    val timestamp: Long
)
