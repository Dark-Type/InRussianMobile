package com.example.inrussian.utils

import com.example.inrussian.models.ErrorType
import dev.icerock.moko.resources.StringResource
import org.example.library.SharedRes

object ErrorDecoder {
    fun decode(errorType: ErrorType): StringResource {
        return when (errorType) {
            ErrorType.InvalidConfirmPassword -> SharedRes.strings.my_string//"Неверное подтверждение пароля"
            ErrorType.InvalidEmail -> SharedRes.strings.my_string// "Неверный email"
            ErrorType.InvalidPassword -> SharedRes.strings.my_string//"Неверный пароль"
            ErrorType.UnAuthorize -> SharedRes.strings.my_string//"Неавторизованный доступ"
            ErrorType.IncorrectCode -> SharedRes.strings.my_string// "Неверный код"
        }
    }
}