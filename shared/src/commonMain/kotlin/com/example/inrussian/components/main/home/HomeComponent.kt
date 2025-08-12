package com.example.inrussian.components.main.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.example.inrussian.di.CourseDetailsComponentFactory
import com.example.inrussian.models.Course
import com.example.inrussian.navigation.configurations.HomeConfiguration
import kotlin.invoke

sealed interface HomeOutput {
    data object NavigateBack : HomeOutput
}

interface HomeComponent {
    val childStack: Value<ChildStack<*, Child>>

    fun onCourseSelected(courseId: String)
    fun onBack()

    sealed interface Child {
        data class CoursesChild(val component: CoursesListComponent) : Child
        data class CourseDetailsChild(val component: CourseDetailsComponent) : Child
    }
}

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeOutput) -> Unit,
    private val courseDetailsComponentFactory: CourseDetailsComponentFactory,
) : HomeComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<HomeConfiguration>()

    override val childStack: Value<ChildStack<*, HomeComponent.Child>> =
        childStack(
            source = navigation,
            serializer = HomeConfiguration.serializer(),
            initialConfiguration = HomeConfiguration.Courses,
            handleBackButton = true,
            childFactory = ::child
        )

    override fun onCourseSelected(courseId: String) {
        navigation.pushNew(HomeConfiguration.CourseDetails(courseId))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) {
            navigation.pop()
        } else {
            onOutput(HomeOutput.NavigateBack)
        }
    }


    private fun child(
        configuration: HomeConfiguration,
        componentContext: ComponentContext
    ): HomeComponent.Child =
        when (configuration) {
            is HomeConfiguration.Courses ->
                HomeComponent.Child.CoursesChild(
                    DefaultCoursesListComponent(
                        componentContext,
                        onCourseClick = ::onCourseSelected
                    )
                )
            is HomeConfiguration.CourseDetails ->
                HomeComponent.Child.CourseDetailsChild(
                    courseDetailsComponentFactory(
                        componentContext,
                        configuration.courseId,
                        ::onCourseDetailsOutput
                    )
                )
        }

    private fun onCourseDetailsOutput(output: CourseDetailsOutput) {
        when (output) {
            is CourseDetailsOutput.NavigateBack -> onBack()
        }
    }
}

interface CoursesListComponent {
    val items: List<Course>
    fun onItemClick(courseId: String)
}

class DefaultCoursesListComponent(
    componentContext: ComponentContext,
    private val onCourseClick: (String) -> Unit
) : CoursesListComponent, ComponentContext by componentContext {

    override val items: List<Course> = listOf(
        Course("c1", "Intro to KMP"),
        Course("c2", "Advanced Compose Multiplatform")
    )

    override fun onItemClick(courseId: String) = onCourseClick(courseId)
}

interface CourseDetailsComponent {
    val courseId: String
    fun onBack()
}
sealed interface CourseDetailsOutput {
    data object NavigateBack : CourseDetailsOutput
}
class DefaultCourseDetailsComponent(
    componentContext: ComponentContext,
    override val courseId: String,
    private val onOutput: (CourseDetailsOutput) -> Unit
) : CourseDetailsComponent, ComponentContext by componentContext {
    override fun onBack() = onOutput(CourseDetailsOutput.NavigateBack)
}