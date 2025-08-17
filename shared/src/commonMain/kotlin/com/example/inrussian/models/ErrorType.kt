package com.example.inrussian.models

sealed class ErrorType : Throwable() {

    data object InvalidEmail : ErrorType(), FieldError, EmailError, AuthError,LoginError

    data object InvalidPassword : ErrorType(), FieldError, PasswordError, AuthError,LoginError

    data object InvalidConfirmPassword : ErrorType(), FieldError, PasswordError, AuthError

    data object UnAuthorize : ErrorType(), NetworkError, AuthError
    data object UnAuthorize1 : ErrorType()

}