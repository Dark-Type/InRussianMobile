package com.example.inrussian.utile

import com.example.inrussian.models.ErrorType
import org.example.library.SharedRes

object ErrorDecoder {
    fun decode(errorType: ErrorType): dev.icerock.moko.resources.StringResource {
        return when (errorType) {
            ErrorType.InvalidConfirmPassword ->
                SharedRes.strings.my_string


            ErrorType.InvalidEmail -> SharedRes.strings.my_string

            ErrorType.InvalidPassword -> SharedRes.strings.my_string

            ErrorType.UnAuthorize -> SharedRes.strings.my_string

            ErrorType.IncorrectCode -> SharedRes.strings.my_string

        }
    }
}