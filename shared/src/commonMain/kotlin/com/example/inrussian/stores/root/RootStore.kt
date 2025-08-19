package com.example.inrussian.stores.root

import com.example.inrussian.navigation.configurations.Configuration
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface RootStore : Store<RootIntent, RootState, RootLabel>

sealed class RootIntent {
    data object ShowAuth : RootIntent()
    data object ShowOnboarding : RootIntent()
    data object ShowMain : RootIntent()
}

data class RootState(
    val currentScreen: Configuration = Configuration.Auth
)

sealed interface RootLabel

private sealed interface Msg {
    data class SetScreen(val configuration: Configuration) : Msg
}

class RootStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): RootStore =
        object : RootStore,
            Store<RootIntent, RootState, RootLabel> by storeFactory.create(
                name = "RootStore",
                initialState = RootState(),
                executorFactory = { ExecutorImpl() },
                reducer = ReducerImpl()
            ) {}

    private class ExecutorImpl :
        Executor<RootIntent, Nothing, RootState, Msg, RootLabel> {

        private lateinit var callbacks: Executor.Callbacks<RootState, Msg, Nothing, RootLabel>

        override fun init(callbacks: Executor.Callbacks<RootState, Msg, Nothing, RootLabel>) {
            this.callbacks = callbacks
        }

        override fun executeIntent(intent: RootIntent) {
            when (intent) {
                RootIntent.ShowAuth -> callbacks.onMessage(Msg.SetScreen(Configuration.Auth))
                RootIntent.ShowOnboarding -> callbacks.onMessage(Msg.SetScreen(Configuration.Onboarding))
                RootIntent.ShowMain -> callbacks.onMessage(Msg.SetScreen(Configuration.Main))
            }
        }

        override fun executeAction(action: Nothing) {
            // No actions (no bootstrapper) — no-op
        }

        override fun dispose() {
            // No resources — no-op
        }
    }

    private class ReducerImpl : Reducer<RootState, Msg> {
        override fun RootState.reduce(msg: Msg): RootState =
            when (msg) {
                is Msg.SetScreen -> copy(currentScreen = msg.configuration)
            }
    }
}