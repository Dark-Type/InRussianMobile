package com.example.inrussian.repository.main.user

import com.example.inrussian.components.main.profile.Gender
import com.example.inrussian.components.main.profile.PeriodSpent
import com.example.inrussian.components.main.profile.UserProfile
import com.example.inrussian.data.client.apis.DefaultApi
import com.example.inrussian.utils.errorHandle
import org.openapitools.client.models.CreateUserProfileRequest

class UserRepositoryImpl(private val api: DefaultApi) : UserRepository {
    override suspend fun updateProfile(profile: UserProfile) {
        api.profilesUserPost(profile.toCreateUserProfileRequest()).errorHandle()
    }
}

fun UserProfile.toCreateUserProfileRequest(): CreateUserProfileRequest {
    return CreateUserProfileRequest(
        surname = this.surname,
        name = this.name,
        gender = when (this.gender) {
            Gender.MALE -> CreateUserProfileRequest.Gender.MALE
            Gender.FEMALE -> CreateUserProfileRequest.Gender.FEMALE
        },
        dob = this.dob,
        dor = this.dor,
        patronymic = this.patronymic,
        citizenship = this.citizenship?.firstOrNull(),
        nationality = this.nationality,
        countryOfResidence = this.countryOfResidence,
        cityOfResidence = this.cityOfResidence,
        countryDuringEducation = this.countryDuringEducation,
        periodSpent = this.periodSpent?.let {
            when (it) {
                PeriodSpent.MONTH_MINUS -> CreateUserProfileRequest.PeriodSpent.MONTH_MINUS
                PeriodSpent.MONTH_SIX_MONTHS_MINUS -> CreateUserProfileRequest.PeriodSpent.MONTH_SIX_MONTHS_MINUS
                PeriodSpent.SIX_MONTHS -> CreateUserProfileRequest.PeriodSpent.SIX_MONTHS
                PeriodSpent.YEAR_MINUS -> CreateUserProfileRequest.PeriodSpent.YEAR_MINUS
                PeriodSpent.YEAR_YEAR_PLUS -> CreateUserProfileRequest.PeriodSpent.YEAR_YEAR_PLUS
                PeriodSpent.YEAR_PLUS -> CreateUserProfileRequest.PeriodSpent.YEAR_PLUS
                PeriodSpent.FIVE_YEAR_PLUS -> CreateUserProfileRequest.PeriodSpent.FIVE_YEAR_PLUS
                PeriodSpent.FIVE_YEARS_PLUS -> CreateUserProfileRequest.PeriodSpent.FIVE_YEARS_PLUS
                PeriodSpent.NEVER -> CreateUserProfileRequest.PeriodSpent.NEVER
            }
        },
        kindOfActivity = this.kindOfActivity,
        education = this.education,
        purposeOfRegister = this.purposeOfRegister
    )
}

