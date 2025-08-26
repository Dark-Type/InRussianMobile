package com.example.inrussian.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.inrussian.domain.Validator
import com.example.inrussian.stores.auth.login.LoginStore
import com.example.inrussian.stores.auth.login.LoginStoreFactory
import com.example.inrussian.stores.auth.recovery.RecoveryStore
import com.example.inrussian.stores.auth.recovery.RecoveryStoreFactory
import com.example.inrussian.stores.auth.register.RegisterStore
import com.example.inrussian.stores.auth.register.RegisterStoreFactory
import com.example.inrussian.stores.main.train.TrainStore
import com.example.inrussian.stores.main.train.TrainStoreFactory
import com.example.inrussian.stores.root.RootStore
import com.example.inrussian.stores.root.RootStoreFactory
import com.example.inrussian.utils.ErrorDecoder
import org.koin.dsl.module

val storeModule = module {
    single<StoreFactory> { DefaultStoreFactory() }
    single<ErrorDecoder> { ErrorDecoder }
    single<Validator> { Validator() }
    factory<RootStore> {
        RootStoreFactory(get()).create()
    }

    factory<LoginStore> {
        LoginStoreFactory(storeFactory = get(), errorDecoder = get(), repository = get()).create()
    }

    factory<RecoveryStore> {
        RecoveryStoreFactory(
            storeFactory = get(),
            errorDecoder = get(),
            validator = get(),
            repository = get()
        ).create()
    }

    factory<TrainStore> { (courseId: String) ->
        TrainStoreFactory(
            storeFactory = get(),
            errorDecoder = get(),
            repository = get(),
        ).create(courseId)
    }

    factory<RegisterStore> {
        RegisterStoreFactory(get(), get(), get(),get()).create()
    }
}