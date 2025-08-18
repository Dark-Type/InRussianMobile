package com.example.inrussian.components.main.profile

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val passwordHash: String = "",
    val phone: String? = null,
    val role: UserRole,
    val systemLanguage: SystemLanguage,
    val avatarId: String? = null,
    val status: UserStatus = UserStatus.ACTIVE,
    val lastActivityAt: String? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class UserProfile(
    val userId: String,
    val surname: String,
    val name: String,
    val patronymic: String? = null,
    val gender: Gender,
    val dob: String,
    val dor: String,
    val citizenship: String? = null,
    val nationality: String? = null,
    val countryOfResidence: String? = null,
    val cityOfResidence: String? = null,
    val countryDuringEducation: String? = null,
    val periodSpent: PeriodSpent? = null,
    val kindOfActivity: String? = null,
    val education: String? = null,
    val purposeOfRegister: String? = null
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
    MONTH_MINUS, MONTH_SIX_MONTHS_MINUS,  SIX_MONTHS, YEAR_MINUS,  YEAR_YEAR_PLUS,  YEAR_PLUS, FIVE_YEAR_PLUS,  FIVE_YEARS_PLUS, NEVER
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
    val theme: AppTheme = AppTheme.SYSTEM
) {
    val displayName: String
        get() = buildString {
            profile?.let {
                append(it.surname)
                append(" ")
                append(it.name)
                it.patronymic?.let { p ->
                    if (p.isNotBlank()) {
                        append(" ")
                        append(p)
                    }
                }
            }
        }.ifBlank { "—" }

    val registrationDate: String
        get() = profile?.dor ?: user?.createdAt ?: "—"
}

data class EditProfileState(
    val isLoading: Boolean = true,
    val original: UserProfile? = null,
    val workingCopy: UserProfile? = null,
    val canSave: Boolean = false,
    val isSaving: Boolean = false
)

data class StaticTextState(
    val text: String,
    val title: String
)