package com.example.inrussian.repository.main.user

import com.example.inrussian.components.main.profile.Gender
import com.example.inrussian.components.main.profile.SystemLanguage
import com.example.inrussian.components.main.profile.User
import com.example.inrussian.components.main.profile.UserProfile
import com.example.inrussian.components.main.profile.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MockUserRepository(
    private val scope: CoroutineScope
) : UserRepository {

    @OptIn(ExperimentalTime::class)
    private val mutableUser = MutableStateFlow(
        User(
            id = "u_1",
            email = "user@example.com",
            role = UserRole.USER,
            systemLanguage = SystemLanguage.RUSSIAN,
            createdAt = Clock.System.now().toString(),
            updatedAt = Clock.System.now().toString()
        )
    )

    @OptIn(ExperimentalTime::class)
    private val mutableProfile = MutableStateFlow(
        UserProfile(
            userId = "u_1",
            surname = "Иванов",
            name = "Иван",
            patronymic = "Иванович",
            gender = Gender.MALE,
            dob = "1995-04-12",
            dor = Clock.System.now().toString().substring(0, 10),
            citizenship = listOf("Россия"),
            nationality = "Русский",
            cityOfResidence = "Москва",
            education = "Высшее",
            purposeOfRegister = "Обучение"
        )
    )

    val userFlow: Flow<User> = mutableUser
    val userProfileFlow: Flow<UserProfile> = mutableProfile


    @OptIn(ExperimentalTime::class)
    override suspend fun updateProfile(profile: UserProfile) {
        delay(400)
        mutableProfile.value = profile.copy(
            userId = mutableProfile.value.userId,
            dor = mutableProfile.value.dor
        )
        mutableUser.value = mutableUser.value.copy(updatedAt = Clock.System.now().toString())
    }

    override suspend fun createProfile(profile: UserProfile) {
        TODO("Not yet implemented")
    }

    init {
        scope.launch {
            userFlow.collect {
            }
        }
    }
}