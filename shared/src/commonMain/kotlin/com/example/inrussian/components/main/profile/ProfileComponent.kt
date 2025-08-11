package com.example.inrussian.components.main.profile

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.main.user.UserRepository

interface ProfileComponent

sealed class ProfileOutput {
    object Logout : ProfileOutput()
}
class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val onOutput: (ProfileOutput) -> Unit,
    private val userRepository: UserRepository
) : ProfileComponent, ComponentContext by componentContext {

    fun onLogoutClicked() {
        onOutput(ProfileOutput.Logout)
    }

}