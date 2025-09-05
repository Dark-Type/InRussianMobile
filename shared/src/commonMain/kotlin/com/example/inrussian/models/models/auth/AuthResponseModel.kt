package com.example.inrussian.models.models.auth

import com.example.inrussian.models.models.auth.UserModel

data class AuthResponseModel(
    val success: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val user: UserModel,
    val message: String,
    val timestamp: Long
)