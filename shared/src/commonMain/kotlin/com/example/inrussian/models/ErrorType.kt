package com.example.inrussian.models

sealed class ErrorType : Throwable() {

    data object InvalidEmail : ErrorType(), FieldError, EmailError, LoginError, RegisterError

    data object InvalidPassword : ErrorType(), FieldError, PasswordError, LoginError, RegisterError,
        PasswordRecoveryError

    data object InvalidConfirmPassword : ErrorType(), FieldError, PasswordError, RegisterError,
        PasswordRecoveryError

    data object UnAuthorize : ErrorType(), NetworkError

    data object IncorrectCode : ErrorType(), PasswordRecoveryError
}