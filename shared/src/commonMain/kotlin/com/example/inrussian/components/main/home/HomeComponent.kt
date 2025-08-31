package com.example.inrussian.components.main.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.inrussian.di.CourseDetailsComponentFactory
import com.example.inrussian.navigation.configurations.HomeConfiguration
import com.example.inrussian.repository.main.home.HomeRepository
import com.example.inrussian.utils.componentCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

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
    private val repository: HomeRepository,
    private val onOutput: (HomeOutput) -> Unit,
    private val courseDetailsComponentFactory: CourseDetailsComponentFactory
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
                        componentContext = componentContext,
                        repository = repository,
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
            CourseDetailsOutput.NavigateBack -> onBack()
        }
    }
}

interface CoursesListComponent {
    val state: Value<CoursesListState>

    fun onRecommendedCourseClick(courseId: String)
    fun onEnrolledCourseClick(courseId: String)
    fun refresh() {}
}


class DefaultCoursesListComponent(
    componentContext: ComponentContext,
    private val repository: HomeRepository,
    private val onCourseClick: (String) -> Unit
) : CoursesListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()

    private val _state = MutableValue(
        CoursesListState(
            recommended = emptyList(),
            enrolled = emptyList(),
            isLoading = true
        )
    )
    override val state: Value<CoursesListState> = _state

    init {
        scope.launch {
            try {
                val recommendedDeferred =
                    async(Dispatchers.IO) { repository.getRecommendedCourses() }
                val enrolledIdsDeferred = async(Dispatchers.IO) { repository.getMyCourses() }

                val recommended = recommendedDeferred.await()
                val enrolledIds = enrolledIdsDeferred.await()

                val enrolledCourses = recommended.filter { it in enrolledIds }
                val recommendedOnly = recommended.filter { it !in enrolledIds }

                _state.value = CoursesListState(
                    recommended = recommendedOnly,
                    enrolled = enrolledCourses,
                    isLoading = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    override fun onRecommendedCourseClick(courseId: String) = onCourseClick(courseId)
    override fun onEnrolledCourseClick(courseId: String) = onCourseClick(courseId)

    fun dispose() {
        scope.cancel()
    }
}

interface CourseDetailsComponent {
    val courseId: String
    val state: Value<CourseDetailsState>

    fun toggleEnroll()
    fun onBack()

    fun showInfo()

    fun signUp()
}

sealed interface CourseDetailsOutput {
    data object NavigateBack : CourseDetailsOutput
}

class DefaultCourseDetailsComponent(
    componentContext: ComponentContext,
    override val courseId: String,
    private val repository: HomeRepository,
    private val onOutput: (CourseDetailsOutput) -> Unit
) : CourseDetailsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()

    private val _state = MutableValue(
        CourseDetailsState(
            course = null,
            isEnrolled = false,
            sections = emptyList(),
            progressPercent = 0,
            isLoading = true
        )
    )
    override val state: Value<CourseDetailsState> = _state

    init {
        scope.launch {
            try {
                val courseDeferred = async(Dispatchers.IO) { repository.courseById(courseId) }
                val myCoursesDeferred = async(Dispatchers.IO) { repository.getMyCourses() }
                val sectionsDeferred = async(Dispatchers.IO) { repository.courseSections(courseId) }
                val progressDeferred =
                    async(Dispatchers.IO) { repository.courseProgressPercent(courseId) }

                val course = courseDeferred.await()
                val myCourses = myCoursesDeferred.await()
                val isEnrolled = myCourses.any { it.id == courseId }
                val sections = sectionsDeferred.await()
                val progress = progressDeferred.await()

                val newState = CourseDetailsState(
                    course = course,
                    isEnrolled = isEnrolled,
                    sections = sections,
                    progressPercent = progress,
                    isLoading = course == null
                )

                _state.value = newState
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    override fun toggleEnroll() {
        val current = _state.value
        val c = current.course ?: return
        scope.launch {
            if (current.isEnrolled) repository.unenroll(c.id) else repository.enroll(c.id)
        }
    }

    override fun onBack() = onOutput(CourseDetailsOutput.NavigateBack)
    override fun showInfo() {
        TODO("Not yet implemented")
    }

    override fun signUp() {
        TODO("Not yet implemented")
    }

    fun dispose() {
        scope.cancel()
    }
}