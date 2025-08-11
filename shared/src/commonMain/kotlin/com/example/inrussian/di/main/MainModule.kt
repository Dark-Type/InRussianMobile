package com.example.inrussian.di.main

import org.koin.dsl.module
import com.example.inrussian.repository.main.user.UserRepository
import com.example.inrussian.repository.main.user.UserRepositoryImpl

val mainModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}