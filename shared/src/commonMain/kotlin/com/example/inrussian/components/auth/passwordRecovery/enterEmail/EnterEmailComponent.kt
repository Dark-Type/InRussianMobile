package com.example.inrussian.components.auth.passwordRecovery.enterEmail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.example.inrussian.stores.auth.recovery.RecoveryStore
import com.example.inrussian.utile.componentCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface EnterEmailComponent {
    val state: StateFlow<RecoveryStore.State>
    fun omEmailChange(email: String)
    fun onBackClicked()
    fun onContinueClick()
}

class DefaultEnterEmailComponent(
    componentContext: ComponentContext,
    private val onOutput: (EnterEmailOutput) -> Unit,
    private val store: RecoveryStore,
) : EnterEmailComponent, ComponentContext by componentContext {
    override val state = MutableStateFlow(store.state)
    override fun omEmailChange(email: String) {
        store.accept(RecoveryStore.Intent.EmailChange(email))
    }

    val scope = componentCoroutineScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    RecoveryStore.Label.SetEmail -> onOutput(EnterEmailOutput.NavigateToRecoveryCode)
                    else -> {}
                }
            }
        }
    }

    override fun onBackClicked() {
        onOutput(EnterEmailOutput.NavigateBack)
    }

    override fun onContinueClick() {
        store.accept(RecoveryStore.Intent.ContinueClick)
    }


}