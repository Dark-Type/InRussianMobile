package com.example.inrussian.models.models
import kotlinx.datetime.*


data class UserProfileModel(
    val surname: String,
    val name: String,
    val patronymic: String?,
    val gender: Gender,
    val dob: LocalDate,
    val dor: LocalDate,
    val citizenship: List<String>,
    val nationality: String,
    val countryOfResidence: String,
    val cityOfResidence: String?,
    val countryDuringEducation: String?,
    val periodSpent: String?,
    val kindOfActivity: String,
    val education: List<String>,
    val purposeOfRegister: String
)
