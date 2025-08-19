package com.example.inrussian.domain

import com.example.inrussian.models.ErrorType

class Validator {
    fun validatePassword(password: String) {
        val validationList = mutableListOf<Boolean>()
        validationList.add(password.length > 6)
        validationList.add(password.any { it.isDigit() })
        validationList.add(password.any { it.isLetter() })
        validationList.add(password.any { !it.isLetterOrDigit() })
        if (validationList.all { it })
            throw ErrorType.InvalidPassword
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
        if (password != confirmPassword)
            throw ErrorType.InvalidConfirmPassword
    }

    fun validateEmail(email: String) {
        if (email.matches(
                Regex("[a-z0-9!#\$%&'*+/=?^_`{|}~\\-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~\\-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
            )
        ) throw ErrorType.InvalidEmail
    }
}