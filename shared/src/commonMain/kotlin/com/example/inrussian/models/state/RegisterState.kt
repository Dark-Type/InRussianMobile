package com.example.inrussian.models.state

data class RegisterState(
    val email: String = "",
    val password: String ="",
    val showPassword: Boolean = false,
    val confirmPassword: String = "",
    val showConfirmPassword: Boolean = false,
    val loading: Boolean = false
)
