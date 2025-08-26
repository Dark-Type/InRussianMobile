package com.example.inrussian.repository

import com.example.inrussian.models.models.AuthResponseModel
import com.example.inrussian.models.models.LoginModel
import com.example.inrussian.models.models.RegisterModel
import com.example.inrussian.models.models.UserModel
import org.openapitools.client.models.LoginRequest
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.StudentRegisterRequest
import org.openapitools.client.models.UserInfo

fun LoginModel.toLoginRequest() = LoginRequest(email = email, password = password)
fun LoginResponse.toAuthResponseModel() = AuthResponseModel(
    success = success,
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toUserInfo(),
    message = message.toString(),
    timestamp = timestamp
)

fun UserInfo.toUserInfo() = UserModel(
    id = id,
    email = email,
    role = role,
    phone = phone.toString(),
    systemLanguage = systemLanguage,
    status = status
)

fun RegisterModel.toRegisterModel() = StudentRegisterRequest(
    email = email,
    password = password,
    phone = phone.toString(),
    systemLanguage = StudentRegisterRequest.SystemLanguage.valueOf(systemLanguage.toString())
)