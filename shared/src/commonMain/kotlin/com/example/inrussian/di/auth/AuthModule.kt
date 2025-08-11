package com.example.inrussian.di.auth

import org.koin.dsl.module
import com.example.inrussian.repository.auth.AuthRepository
import com.example.inrussian.repository.auth.AuthRepositoryImpl

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
}