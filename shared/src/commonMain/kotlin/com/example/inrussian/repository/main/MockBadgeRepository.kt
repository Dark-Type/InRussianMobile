package com.example.inrussian.repository.main

import com.example.inrussian.components.main.profile.Badge
import com.example.inrussian.components.main.profile.BadgeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MockBadgeRepository(
    private val scope: CoroutineScope
) : BadgeRepository {

    @OptIn(ExperimentalTime::class)
    private val badgePool = listOf(
        Badge(
            "b1",
            "Новичок",
            iconUrl = "https://example.com/badges/newbie.png",
            badgeType = BadgeType.ACHIEVEMENT,
            createdAt = Clock.System.now().toString()
        ),
        Badge("b2", "Усердный", iconUrl = "", badgeType = BadgeType.ACHIEVEMENT, createdAt = Clock.System.now().toString()),
        Badge("b3", "Первые шаги", iconUrl = "", badgeType = BadgeType.THEME_COMPLETION, createdAt = Clock.System.now().toString()),
        Badge("b4", "Эксперт", iconUrl = "", badgeType = BadgeType.COURSE_COMPLETION, createdAt = Clock.System.now().toString())
    )

    override fun badgesForUser(userId: String): Flow<List<Badge>> =
        flow {
            delay(300)
            val count = Random.Default.nextInt(1, badgePool.size + 1)
            emit(badgePool.shuffled().take(count))
        }
}