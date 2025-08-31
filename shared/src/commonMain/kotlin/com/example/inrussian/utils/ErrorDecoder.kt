package com.example.inrussian.utils

import co.touchlab.kermit.Logger
import com.example.inrussian.models.ErrorType
import com.example.inrussian.models.ErrorType.EmailExist
import com.example.inrussian.models.ErrorType.IncorrectCode
import com.example.inrussian.models.ErrorType.InvalidConfirmPassword
import com.example.inrussian.models.ErrorType.InvalidEmail
import com.example.inrussian.models.ErrorType.InvalidPassword
import com.example.inrussian.models.ErrorType.InvalidPhone
import com.example.inrussian.models.ErrorType.OtherErrors
import com.example.inrussian.models.ErrorType.UnAuthorize
import dev.icerock.moko.resources.StringResource
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.library.SharedRes

object ErrorDecoder {
    fun toErrorType(errorBody: String): Nothing {
        val error = try {
            Json.decodeFromString<ErrorResponse>(errorBody).error
        } catch (e: Throwable) {
            Json.decodeFromString<ValidationErrorResponse>(errorBody).errors.first().message
        } catch (e: Throwable) {
            Logger.i { e.message.toString() }
        }

        throw when (error) {
            "Invalid email or password" -> InvalidEmail
            "{email.invalid}" -> InvalidEmail
            "{password.special}" -> InvalidPassword
            "{password.digit}" -> InvalidPassword
            "{phone.invalid}" -> InvalidPhone
            "User with this email already exists" -> EmailExist
            else -> OtherErrors
        }
    }

    fun decode(errorType: ErrorType): StringResource {
        return when (errorType) {
            InvalidConfirmPassword -> SharedRes.strings.invalid_confirm_password
            InvalidEmail -> SharedRes.strings.invalid_email
            InvalidPassword -> SharedRes.strings.invalid_password
            UnAuthorize -> SharedRes.strings.my_string
            IncorrectCode -> SharedRes.strings.my_string
            OtherErrors -> SharedRes.strings.my_string
            InvalidPhone -> SharedRes.strings.invalid_phone
            EmailExist -> SharedRes.strings.invalid_email
            ErrorType.InvalidRefreshToken -> TODO()
        }
    }
}

@Serializable
data class ErrorResponse(
    val success: Boolean,
    val error: String?,
    val timestamp: Long?
)

@Serializable
data class ValidationErrorResponse(
    val errors: List<FieldError>
)


@Serializable
data class FieldError(
    val field: String,
    val code: String,
    val message: String
)