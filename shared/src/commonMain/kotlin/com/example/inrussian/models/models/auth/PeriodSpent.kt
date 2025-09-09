package com.example.inrussian.models.models.auth

import dev.icerock.moko.resources.StringResource
import kotlinx.serialization.Serializable
import org.example.library.SharedRes

@Serializable
enum class PeriodSpent(val string: StringResource) {
    
    MONTH_MINUS(SharedRes.strings.month_minus), MONTH_SIX_MONTHS_MINUS(SharedRes.strings.month_six_month_minus), SIX_MONTHS(
        SharedRes.strings.six_month
    ),
    YEAR_MINUS(SharedRes.strings.year_minus), YEAR_PLUS(SharedRes.strings.year_plus), FIVE_YEARS_PLUS(
        SharedRes.strings.five_years_plus
    ),
    NEVER(SharedRes.strings.never)
}
