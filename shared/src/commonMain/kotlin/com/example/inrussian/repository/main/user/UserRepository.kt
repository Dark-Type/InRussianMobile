package com.example.inrussian.repository.main.user

import com.example.inrussian.components.main.profile.User
import com.example.inrussian.components.main.profile.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userFlow: Flow<User>
    val userProfileFlow: Flow<UserProfile>
    suspend fun updateProfile(profile: UserProfile)
}
