package com.example.inrussian.models

data class EnterRecoveryCodeState(
    val email: String,
    val code: String,
    val timer: String,
    val sendButtonEnable: Boolean,
    val showHint: Boolean
)