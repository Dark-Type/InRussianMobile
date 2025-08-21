package com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.example.inrussian.stores.auth.recovery.RecoveryStore
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Intent
import com.example.inrussian.utils.asValue
import com.example.inrussian.utils.componentCoroutineScope

import kotlinx.coroutines.launch

interface EnterRecoveryCodeComponent {
    val state: Value<RecoveryStore.State>

    fun onCodeEntered(code: String)
    fun onBackClicked()

    fun onQuestionClick()

    fun codeChange(code: String)

    fun onMissClick()

    fun onSupportContactClick()
}

sealed class EnterRecoveryCodeOutput {
    object NavigateToUpdatePassword : EnterRecoveryCodeOutput()
    object NavigateBack : EnterRecoveryCodeOutput()
}

class DefaultEnterRecoveryCodeComponent(
    componentContext: ComponentContext,
    private val onOutput: (EnterRecoveryCodeOutput) -> Unit,
    private val store: RecoveryStore,
) : EnterRecoveryCodeComponent, ComponentContext by componentContext {
    override val state = store.asValue()
    val scope = componentCoroutineScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    RecoveryStore.Label.SetCorrectCode -> onOutput(EnterRecoveryCodeOutput.NavigateToUpdatePassword)
                    else -> {}
                }

            }
        }
    }

    override fun onCodeEntered(code: String) {
        store.accept(Intent.ContinueClick)
        onOutput(EnterRecoveryCodeOutput.NavigateToUpdatePassword)
    }

    override fun onBackClicked() {
        onOutput(EnterRecoveryCodeOutput.NavigateBack)
    }

    override fun onQuestionClick() {
        store.accept(Intent.QuestionClick)
    }

    override fun codeChange(code: String) {
        store.accept(Intent.CodeChange(code))
    }

    override fun onMissClick() {
        store.accept(Intent.QuestionDismiss)
    }

    override fun onSupportContactClick() {
        store.accept(Intent.QuestionButtonClick)
    }

}