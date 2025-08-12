package com.example.inrussian.components.main.train

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.example.inrussian.di.SectionDetailComponentFactory
import com.example.inrussian.di.TasksComponentFactory
import com.example.inrussian.models.Course
import com.example.inrussian.models.Section
import com.example.inrussian.navigation.configurations.TrainConfiguration

sealed interface TrainOutput {
    data object NavigateBack : TrainOutput
}
sealed interface SectionDetailOutput {
    data object NavigateBack : SectionDetailOutput
}



interface TrainComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun onSectionSelected(sectionId: String)
    fun onBack()

    sealed interface Child {
        data class CoursesChild(val component: TrainCoursesListComponent) : Child
        data class SectionDetailChild(val component: SectionDetailComponent) : Child
    }
}

class DefaultTrainComponent(
    componentContext: ComponentContext,
    private val onOutput: (TrainOutput) -> Unit,
    private val sectionDetailComponentFactory: SectionDetailComponentFactory,
) : TrainComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<TrainConfiguration>()

    override val childStack: Value<ChildStack<*, TrainComponent.Child>> =
        childStack(
            source = navigation,
            serializer = TrainConfiguration.serializer(),
            initialConfiguration = TrainConfiguration.Courses,
            handleBackButton = true,
            childFactory = ::child
        )

    override fun onSectionSelected(sectionId: String) {
        navigation.pushNew(TrainConfiguration.SectionDetail(sectionId))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) {
            navigation.pop()
        } else {
            onOutput(TrainOutput.NavigateBack)
        }
    }

    private fun child(
        configuration: TrainConfiguration,
        componentContext: ComponentContext
    ): TrainComponent.Child =
        when (configuration) {
            is TrainConfiguration.Courses ->
                TrainComponent.Child.CoursesChild(
                    DefaultTrainCoursesListComponent(
                        componentContext,
                        onSectionClick = ::onSectionSelected
                    )
                )
            is TrainConfiguration.SectionDetail ->
                TrainComponent.Child.SectionDetailChild(
                    sectionDetailComponentFactory(componentContext, configuration.sectionId) { output ->
                        if (output is SectionDetailOutput.NavigateBack) onBack()
                    }
                )
        }
}

interface TrainCoursesListComponent {
    val items: List<Course>
    val sections: List<Section>
    fun onSectionClick(sectionId: String)
}

class DefaultTrainCoursesListComponent(
    componentContext: ComponentContext,
    private val onSectionClick: (String) -> Unit
) : TrainCoursesListComponent, ComponentContext by componentContext {

    override val items: List<Course> = listOf(
        Course("c1", "KMP Practice"),
        Course("c2", "Compose Practice")
    )

    override val sections: List<Section> = listOf(
        Section("s1", "Basics Section"),
        Section("s2", "Coroutines Section"),
        Section("s3", "Compose Section")
    )

    override fun onSectionClick(sectionId: String) = onSectionClick.invoke(sectionId)
}


interface SectionDetailComponent {
    val sectionId: String
    fun openTasks(option: TasksOption)
    fun onBack()
    val onOutput: (SectionDetailOutput) -> Unit
}

class DefaultSectionDetailComponent(
    componentContext: ComponentContext,
    override val sectionId: String,
    override val onOutput: (SectionDetailOutput) -> Unit = {},
    private val tasksFactory: TasksComponentFactory = { ctx, sectionId, option, onOutput ->
        DefaultTasksComponent(ctx, sectionId = sectionId, option = option, onOutput = onOutput)
    }
) : SectionDetailComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<InnerConfig>()

    val childStack: Value<ChildStack<InnerConfig, InnerChild>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = InnerConfig.Details,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun openTasks(option: TasksOption) {
        navigation.pushNew(InnerConfig.Tasks(option))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) {
            navigation.pop()
        } else {
            onOutput(SectionDetailOutput.NavigateBack)
        }
    }

    private fun createChild(
        config: InnerConfig,
        ctx: ComponentContext
    ): InnerChild =
        when (config) {
            InnerConfig.Details -> InnerChild.DetailsChild(this)
            is InnerConfig.Tasks -> InnerChild.TasksChild(
                tasksFactory(ctx, sectionId, config.option) { output ->
                    if (output is TasksOutput.NavigateBack) onBack()
                }
            )
        }

    sealed interface InnerConfig {
        data object Details : InnerConfig
        data class Tasks(val option: TasksOption) : InnerConfig
    }

    sealed interface InnerChild {
        data class DetailsChild(val component: SectionDetailComponent) : InnerChild
        data class TasksChild(val component: TasksComponent) : InnerChild
    }
}

enum class TasksOption {
    All, Unsolved, Favorites
}

sealed interface TasksOutput {
    data object NavigateBack : TasksOutput
}

interface TasksComponent {
    val sectionId: String
    val option: TasksOption
    fun onBack()
    val onOutput: (TasksOutput) -> Unit
}

class DefaultTasksComponent(
    componentContext: ComponentContext,
    override val sectionId: String,
    override val option: TasksOption,
    override val onOutput: (TasksOutput) -> Unit = {}
) : TasksComponent, ComponentContext by componentContext {
    override fun onBack() {
        onOutput(TasksOutput.NavigateBack)
    }
}