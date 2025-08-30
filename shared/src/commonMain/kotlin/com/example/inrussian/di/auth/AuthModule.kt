package com.example.inrussian.di.auth

import com.example.inrussian.data.client.apis.DefaultApi
import com.example.inrussian.repository.auth.AuthRepository
import com.example.inrussian.repository.auth.AuthRepositoryImpl
import org.koin.dsl.module

val authModule = module {
    single<DefaultApi> { DefaultApi() }
    single<AuthRepository> { AuthRepositoryImpl(get(),get()) }
}