package com.example.inrussian.components.main.profile

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val passwordHash: String = "",
    val phone: String? = null,
    val role: UserRole = UserRole.USER,
    val systemLanguage: SystemLanguage = SystemLanguage.RUSSIAN,
    val avatarId: String? = null,
    val status: UserStatus = UserStatus.ACTIVE,
    val lastActivityAt: String? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
)

@Serializable
data class UserProfile(
    val userId: String = "",
    val surname: String = "",
    val name: String = "",
    val patronymic: String? = null,
    val gender: Gender = Gender.MALE,
    val dob: String = "",
    val dor: String = "",
    val citizenship: List<String>? = null,
    val nationality: String? = null,
    val countryOfResidence: String? = null,
    val cityOfResidence: String? = null,
    val countryDuringEducation: String? = null,
    val periodSpent: PeriodSpent? = null,
    val kindOfActivity: String? = null,
    val education: String? = null,
    val purposeOfRegister: String? = null,
    val language: SystemLanguage = SystemLanguage.RUSSIAN,
    val languages: List<String> = listOf(),
    val email: String = "",
)

@Serializable
data class Badge(
    val id: String,
    val name: String,
    val description: String? = null,
    val iconUrl: String,
    val badgeType: BadgeType,
    val criteria: JsonElement? = null,
    val createdAt: String
)

enum class UserRole { USER }

@Serializable
enum class SystemLanguage {
    RUSSIAN, UZBEK, CHINESE, HINDI, TAJIK, ENGLISH
}


enum class UserStatus {
    ACTIVE,
    SUSPENDED,
    DEACTIVATED,
    PENDING_VERIFICATION
}

enum class Gender { MALE, FEMALE }
enum class PeriodSpent {
    MONTH_MINUS, MONTH_SIX_MONTHS_MINUS, SIX_MONTHS, YEAR_MINUS, YEAR_YEAR_PLUS, YEAR_PLUS, FIVE_YEAR_PLUS, FIVE_YEARS_PLUS, NEVER
}

enum class BadgeType {
    COURSE_COMPLETION, THEME_COMPLETION, STREAK, ACHIEVEMENT
}

enum class AppTheme { SYSTEM, LIGHT, DARK }

data class ProfileMainState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val profile: UserProfile? = null,
    val badges: List<Badge> = emptyList(),
    val notificationsEnabled: Boolean = false,
    val theme: AppTheme = AppTheme.SYSTEM,
    val isEditThemeOpen: Boolean = false,
) {
    val displayName: String get() = "${profile?.surname} ${profile?.name}\n${profile?.patronymic}".ifBlank { "—" }

    val registrationDate: String
        get() = profile?.dor ?: user?.createdAt ?: "—"
}

data class EditProfileState(
    val isLoading: Boolean = true,
    val original: UserProfile? = null,
    val workingCopy: UserProfile? = null,
    val canSave: Boolean = false,
    val isSaving: Boolean = false,
    val isGenderOpen: Boolean = false,
    val isDateOpen: Boolean = false,
    val isCitizenshipOpen: Boolean = false,
    val isNationalityOpen: Boolean = false,
    val isTimeOpen: Boolean = false,
    val isLanguageOpen: Boolean = false,
)

data class StaticTextState(
    val text: String,
    val title: String
)