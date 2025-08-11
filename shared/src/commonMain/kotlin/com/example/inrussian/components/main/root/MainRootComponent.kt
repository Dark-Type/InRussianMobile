package com.example.inrussian.components.main.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.Value
import com.example.inrussian.navigation.configurations.MainConfiguration
import com.example.inrussian.components.main.home.HomeComponent
import com.example.inrussian.components.main.home.HomeOutput
import com.example.inrussian.components.main.profile.ProfileComponent
import com.example.inrussian.components.main.profile.ProfileOutput
import com.example.inrussian.components.main.train.TrainComponent
import com.example.inrussian.components.main.train.TrainOutput
interface MainRootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun selectTab(configuration: MainConfiguration)

    sealed class Child {
        class HomeChild(val component: HomeComponent) : Child()
        class TrainChild(val component: TrainComponent) : Child()
        class ProfileChild(val component: ProfileComponent) : Child()
    }
}

class DefaultMainRootComponent(
    componentContext: ComponentContext,
    private val onOutput: (MainOutput) -> Unit,
    private val homeComponentFactory: (ComponentContext, (HomeOutput) -> Unit) -> HomeComponent,
    private val trainComponentFactory: (ComponentContext, (TrainOutput) -> Unit) -> TrainComponent,
    private val profileComponentFactory: (ComponentContext, (ProfileOutput) -> Unit) -> ProfileComponent,
) : MainRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainConfiguration>()

    override val stack: Value<ChildStack<*, MainRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = MainConfiguration.serializer(),
            initialConfiguration = MainConfiguration.Home,
            handleBackButton = false,
            childFactory = ::child,
        )

    private fun child(configuration: MainConfiguration, componentContext: ComponentContext): MainRootComponent.Child =
        when (configuration) {
            is MainConfiguration.Home -> MainRootComponent.Child.HomeChild(
                homeComponentFactory(componentContext, ::onHomeOutput)
            )
            is MainConfiguration.Train -> MainRootComponent.Child.TrainChild(
                trainComponentFactory(componentContext, ::onTrainOutput)
            )
            is MainConfiguration.Profile -> MainRootComponent.Child.ProfileChild(
                profileComponentFactory(componentContext, ::onProfileOutput)
            )
        }

    override fun selectTab(configuration: MainConfiguration) {
        navigation.navigate { listOf(configuration) }
    }

    private fun onHomeOutput(output: HomeOutput): Unit =
        when (output) {
            //TODO: Handle home-specific navigation
        }


    private fun onTrainOutput(output: TrainOutput): Unit =
        when (output) {
            //TODO: Handle train-specific navigation
        }

    private fun onProfileOutput(output: ProfileOutput): Unit =
        when (output) {
            is ProfileOutput.Logout -> onOutput(MainOutput.Logout)
        }
}