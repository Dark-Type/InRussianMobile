package com.example.inrussian.components.main.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.example.inrussian.navigation.configurations.MainConfiguration
import com.example.inrussian.components.main.home.HomeComponent
import com.example.inrussian.components.main.home.HomeOutput
import com.example.inrussian.components.main.profile.ProfileComponent
import com.example.inrussian.components.main.profile.ProfileOutput
import com.example.inrussian.components.main.train.TrainComponent
import com.example.inrussian.components.main.train.TrainOutput
import com.example.inrussian.di.HomeFactory
import com.example.inrussian.di.ProfileFactory
import com.example.inrussian.di.TrainFactory




interface MainRootComponent {
    val childStack: Value<ChildStack<*, Child>>
    val activeTab: Value<Tab>

    fun openTab(tab: Tab)

    enum class Tab { Home, Train, Profile }

    sealed interface Child {
        data class HomeChild(val component: HomeComponent) : Child
        data class TrainChild(val component: TrainComponent) : Child
        data class ProfileChild(val component: ProfileComponent) : Child
    }
}

class DefaultMainRootComponent(
    componentContext: ComponentContext,
    private val onOutput: (MainOutput) -> Unit,
    private val homeFactory: HomeFactory,
    private val trainFactory: TrainFactory,
    private val profileFactory: ProfileFactory
) : MainRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainConfiguration>()

    override val childStack: Value<ChildStack<*, MainRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = MainConfiguration.serializer(),
            initialConfiguration = MainConfiguration.Home,
            handleBackButton = true,
            childFactory = ::child
        )

    override val activeTab: Value<MainRootComponent.Tab> = childStack.map { stack ->
        when (stack.active.configuration) {
            is MainConfiguration.Home -> MainRootComponent.Tab.Home
            is MainConfiguration.Train -> MainRootComponent.Tab.Train
            is MainConfiguration.Profile -> MainRootComponent.Tab.Profile
            else -> MainRootComponent.Tab.Home
        }
    }

    override fun openTab(tab: MainRootComponent.Tab) {
        val config = when (tab) {
            MainRootComponent.Tab.Home -> MainConfiguration.Home
            MainRootComponent.Tab.Train -> MainConfiguration.Train
            MainRootComponent.Tab.Profile -> MainConfiguration.Profile
        }
        navigation.bringToFront(config)
    }

    private fun child(
        configuration: MainConfiguration,
        ctx: ComponentContext
    ): MainRootComponent.Child =
        when (configuration) {
            is MainConfiguration.Home -> MainRootComponent.Child.HomeChild(
                homeFactory(ctx, ::onHomeOutput)
            )
            is MainConfiguration.Train -> MainRootComponent.Child.TrainChild(
                trainFactory(ctx, ::onTrainOutput)
            )
            is MainConfiguration.Profile -> MainRootComponent.Child.ProfileChild(
                profileFactory(ctx, ::onProfileOutput)
            )
        }

    private fun onHomeOutput(output: HomeOutput) {
        when (output) {
            is HomeOutput.NavigateBack -> onOutput(MainOutput.NavigateBack)
        }
    }

    private fun onTrainOutput(output: TrainOutput) {
        when (output) {
            is TrainOutput.NavigateBack -> onOutput(MainOutput.NavigateBack)
        }
    }

    private fun onProfileOutput(output: ProfileOutput) {
        when (output) {
            is ProfileOutput.NavigateBack -> onOutput(MainOutput.NavigateBack)
        }
    }
}

private inline fun <T : Any, R : Any> Value<T>.map(crossinline transformer: (T) -> R): Value<R> =
    object : Value<R>() {
        override val value: R get() = transformer(this@map.value)
        override fun subscribe(observer: (R) -> Unit) =
            this@map.subscribe { observer(transformer(it)) }
    }