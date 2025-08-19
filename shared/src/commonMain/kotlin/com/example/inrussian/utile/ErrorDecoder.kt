package com.example.inrussian.utile

import com.example.inrussian.models.ErrorType

object ErrorDecoder {
    fun decode(errorType: ErrorType): String {
        return when (errorType) {
            ErrorType.InvalidConfirmPassword -> "Неверное подтверждение пароля"
            ErrorType.InvalidEmail -> "Неверный email"
            ErrorType.InvalidPassword -> "Неверный пароль"
            ErrorType.UnAuthorize -> "Неавторизованный доступ"
            ErrorType.IncorrectCode -> "Неверный код"
        }
    }
}