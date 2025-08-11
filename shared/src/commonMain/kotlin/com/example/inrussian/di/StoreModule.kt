package com.example.inrussian.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.inrussian.stores.root.RootStore
import com.example.inrussian.stores.root.RootStoreFactory
import org.koin.dsl.module

val storeModule = module {
    single<StoreFactory> { DefaultStoreFactory() }

    factory<RootStore> {
        RootStoreFactory(get()).create()
    }
}