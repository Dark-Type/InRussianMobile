package com.example.inrussian.repository.main.user

import com.example.inrussian.components.main.profile.UserProfile

interface UserRepository {
    suspend fun updateProfile(profile: UserProfile)
    suspend fun createProfile(profile: UserProfile)
}
