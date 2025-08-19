package com.example.inrussian.models.models

data class RegisterModel(
    val email: String,
    val password: String,
    val phone: String,
    val systemLanguage: SystemLanguage
)
