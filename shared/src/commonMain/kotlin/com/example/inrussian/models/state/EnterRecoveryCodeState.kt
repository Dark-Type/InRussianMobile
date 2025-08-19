package com.example.inrussian.models.state

data class EnterRecoveryCodeState(
    val email: String,
    val code: String,
    val timer: String,
    val sendButtonEnable: Boolean,
    val showHint: Boolean
)