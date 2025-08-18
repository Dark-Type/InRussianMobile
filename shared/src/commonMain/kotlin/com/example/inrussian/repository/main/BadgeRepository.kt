package com.example.inrussian.repository.main

import com.example.inrussian.components.main.profile.Badge
import kotlinx.coroutines.flow.Flow

interface BadgeRepository {
    fun badgesForUser(userId: String): Flow<List<Badge>>
}