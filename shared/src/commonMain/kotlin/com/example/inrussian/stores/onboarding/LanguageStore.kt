package com.example.inrussian.stores.onboarding

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface LanguageStore : Store<LanguageIntent, LanguageState, LanguageLabel>

sealed class LanguageIntent {
    data class SelectLanguage(val language: String) : LanguageIntent()
    object Next : LanguageIntent()
    object Back : LanguageIntent()
}

data class LanguageState(
    val selectedLanguage: String = ""
)

sealed interface LanguageLabel {
    object Filled : LanguageLabel
    object Back : LanguageLabel
}

private sealed interface Msg {
    data class SetLanguage(val language: String) : Msg
}

class LanguageStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): LanguageStore =
        object : LanguageStore,
            Store<LanguageIntent, LanguageState, LanguageLabel> by storeFactory.create(
                name = "LanguageStore",
                initialState = LanguageState(),
                executorFactory = { ExecutorImpl() },
                reducer = ReducerImpl()
            ) {}

    private class ExecutorImpl :
        Executor<LanguageIntent, Nothing, LanguageState, Msg, LanguageLabel> {

        private lateinit var callbacks: Executor.Callbacks<LanguageState, Msg, Nothing, LanguageLabel>

        override fun init(callbacks: Executor.Callbacks<LanguageState, Msg, Nothing, LanguageLabel>) {
            this.callbacks = callbacks
        }

        override fun executeIntent(intent: LanguageIntent) {
            when (intent) {
                is LanguageIntent.SelectLanguage -> callbacks.onMessage(Msg.SetLanguage(intent.language))
                LanguageIntent.Next -> callbacks.onLabel(LanguageLabel.Filled)
                LanguageIntent.Back -> callbacks.onLabel(LanguageLabel.Back)
            }
        }

        override fun executeAction(action: Nothing) {}
        override fun dispose() {}
    }

    private class ReducerImpl : Reducer<LanguageState, Msg> {
        override fun LanguageState.reduce(msg: Msg): LanguageState =
            when (msg) {
                is Msg.SetLanguage -> copy(selectedLanguage = msg.language)
            }
    }
}