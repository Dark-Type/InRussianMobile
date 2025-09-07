package com.example.inrussian.repository.main.user

import com.example.inrussian.components.main.profile.UserProfile
import com.example.inrussian.data.client.apis.DefaultApi
import com.example.inrussian.utils.errorHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.openapitools.client.models.CreateUserProfileRequest
import org.openapitools.client.models.UpdateUserProfileRequest
import org.openapitools.client.models.UserProfileModel

class UserRepositoryImpl(
    private val api: DefaultApi
) : UserRepository {
    private val mutableUser = MutableStateFlow<UserProfileModel?>(null)

    override val userFlow: Flow<UserProfileModel> = mutableUser.filterNotNull()


    override suspend fun getProfile() {
        val response = api.profilesUserGet().body()
        if (response.success) {
            mutableUser.value = response.profile
        }
    }

    override suspend fun updateProfileLocal(profile: UserProfileModel) {
        mutableUser.value = profile
    }



    override suspend fun updateProfile(profile: UserProfileModel) {
        api.profilesUserIdPut(profile.userId, profile.toUpdateUserProfileRequest())
            .errorHandle()
    }

    override suspend fun createProfile(profile: UserProfile) {
        api.profilesUserPost(profile.toCreateUserProfileRequest())
            .errorHandle()
    }
}

fun UserProfile.toCreateUserProfileRequest(): CreateUserProfileRequest {
    return CreateUserProfileRequest(
        surname = this.surname,
        name = this.name,
        gender = when (this.gender) {
            org.openapitools.client.models.UserProfileModel.Gender.MALE -> CreateUserProfileRequest.Gender.MALE
            org.openapitools.client.models.UserProfileModel.Gender.FEMALE -> CreateUserProfileRequest.Gender.FEMALE
        },
        dob = this.dob,
        dor = this.dor,
        patronymic = this.patronymic,
        citizenship = this.citizenship?.firstOrNull() as String?,
        nationality = this.nationality,
        countryOfResidence = this.countryOfResidence,
        cityOfResidence = this.cityOfResidence,
        countryDuringEducation = this.countryDuringEducation,
        periodSpent = this.periodSpent?.let {
            when (it) {
                org.openapitools.client.models.UserProfileModel.PeriodSpent.MONTH_MINUS -> CreateUserProfileRequest.PeriodSpent.MONTH_MINUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.MONTH_SIX_MONTHS_MINUS -> CreateUserProfileRequest.PeriodSpent.MONTH_SIX_MONTHS_MINUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.SIX_MONTHS -> CreateUserProfileRequest.PeriodSpent.SIX_MONTHS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.YEAR_MINUS -> CreateUserProfileRequest.PeriodSpent.YEAR_MINUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.YEAR_YEAR_PLUS -> CreateUserProfileRequest.PeriodSpent.YEAR_YEAR_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.YEAR_PLUS -> CreateUserProfileRequest.PeriodSpent.YEAR_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.FIVE_YEAR_PLUS -> CreateUserProfileRequest.PeriodSpent.FIVE_YEAR_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.FIVE_YEARS_PLUS -> CreateUserProfileRequest.PeriodSpent.FIVE_YEARS_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.NEVER -> CreateUserProfileRequest.PeriodSpent.NEVER
            }
        },
        kindOfActivity = this.kindOfActivity,
        education = this.education,
        purposeOfRegister = this.purposeOfRegister
    )
}


fun UserProfileModel.toUpdateUserProfileRequest(): UpdateUserProfileRequest {
    return UpdateUserProfileRequest(
        surname = this.surname,
        name = this.name,
        gender = when (this.gender) {
            org.openapitools.client.models.UserProfileModel.Gender.MALE -> UpdateUserProfileRequest.Gender.MALE
            org.openapitools.client.models.UserProfileModel.Gender.FEMALE -> UpdateUserProfileRequest.Gender.FEMALE
        },
        dob = this.dob,
        dor = this.dor,
        patronymic = this.patronymic,
        citizenship = this.citizenship?.firstOrNull() as String?,
        nationality = this.nationality,
        countryOfResidence = this.countryOfResidence,
        cityOfResidence = this.cityOfResidence,
        countryDuringEducation = this.countryDuringEducation,
        periodSpent = this.periodSpent?.let {
            when (it) {
                org.openapitools.client.models.UserProfileModel.PeriodSpent.MONTH_MINUS -> UpdateUserProfileRequest.PeriodSpent.MONTH_MINUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.MONTH_SIX_MONTHS_MINUS -> UpdateUserProfileRequest.PeriodSpent.MONTH_SIX_MONTHS_MINUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.SIX_MONTHS -> UpdateUserProfileRequest.PeriodSpent.SIX_MONTHS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.YEAR_MINUS -> UpdateUserProfileRequest.PeriodSpent.YEAR_MINUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.YEAR_YEAR_PLUS -> UpdateUserProfileRequest.PeriodSpent.YEAR_YEAR_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.YEAR_PLUS -> UpdateUserProfileRequest.PeriodSpent.YEAR_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.FIVE_YEAR_PLUS -> UpdateUserProfileRequest.PeriodSpent.FIVE_YEAR_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.FIVE_YEARS_PLUS -> UpdateUserProfileRequest.PeriodSpent.FIVE_YEARS_PLUS
                org.openapitools.client.models.UserProfileModel.PeriodSpent.NEVER -> UpdateUserProfileRequest.PeriodSpent.NEVER
            }
        },
        kindOfActivity = this.kindOfActivity,
        education = this.education,
        purposeOfRegister = this.purposeOfRegister
    )
}
