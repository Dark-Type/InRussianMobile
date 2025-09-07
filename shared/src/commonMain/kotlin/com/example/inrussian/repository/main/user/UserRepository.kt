package com.example.inrussian.repository.main.user


import com.example.inrussian.components.main.profile.UserProfile
import kotlinx.coroutines.flow.Flow
import org.openapitools.client.models.UserProfileModel


interface UserRepository {

    suspend fun getProfile()

    suspend fun updateProfileLocal(profile: UserProfileModel)
    suspend fun updateProfile(profile: UserProfileModel)
    suspend fun createProfile(profile: UserProfile)

    val userFlow: Flow<UserProfileModel>
}
