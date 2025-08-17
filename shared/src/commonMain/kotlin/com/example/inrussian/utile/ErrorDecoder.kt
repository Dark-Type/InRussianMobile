package com.example.inrussian.utile

import com.example.inrussian.models.ErrorType
import org.jetbrains.compose.resources.StringResource

class ErrorDecoder {
    fun decode(errorType: ErrorType): StringResource {
        return when(errorType){
            ErrorType.InvalidConfirmPassword -> TODO()
            ErrorType.InvalidEmail -> TODO()
            ErrorType.InvalidPassword -> TODO()
            ErrorType.UnAuthorize -> TODO()
            ErrorType.UnAuthorize1 -> TODO()
        }
    }
}