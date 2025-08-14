package com.example.inrussian.di

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.components.root.RootComponent
import com.example.inrussian.di.auth.authModule
import com.example.inrussian.di.main.mainModule
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object SharedDI {
    private var koinApp: KoinApplication? = null

    val koin: Koin
        get() = koinApp?.koin
            ?: error("Koin is not started. Call SharedDI.start(...) on platform startup first.")
    fun start(vararg platformModules: Module) {
        if (koinApp == null) {
            koinApp = startKoin {
                modules(
                    listOf(
                        authModule,
                        mainModule,
                        storeModule,
                        navigationModule
                    ) + platformModules
                )
            }
        }
    }

    fun stop() {
        koinApp?.close()
        koinApp = null
    }

    fun createRoot(componentContext: ComponentContext): RootComponent {
        val rootFactory: (ComponentContext) -> RootComponent = koin.get()
        return rootFactory(componentContext)
    }
}