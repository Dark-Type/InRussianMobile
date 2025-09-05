package com.example.inrussian.models.models.auth

data class RegisterModel(
    val email: String,
    val password: String,
    val phone: String,
    val systemLanguage: SystemLanguage
)