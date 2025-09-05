package com.example.inrussian.models.models.auth

data class UserModel(
    val id: String,
    val email: String,
    val role: String,
    val phone: String,
    val systemLanguage: String,
    val status: String
)