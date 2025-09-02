package com.example.inrussian.models.state

data class UpdatePasswordState(
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val updateButtonEnable: Boolean = false,
    val isLoading: Boolean = false,
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val confirmPasswordError: String? = null
)