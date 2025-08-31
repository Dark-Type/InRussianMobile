package com.example.inrussian.data.client

import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

class TokenRefresher(
    private val tokenStorage: UserConfigurationStorage,
    private val refreshCall: suspend (refreshToken: String) -> String?,
    private val timeoutMs: Long = 10_000L
) {
    private val mutex = Mutex()

    suspend fun refreshIfNeeded(currentRefreshToken: String?): Boolean {
        val refresh = currentRefreshToken ?: return false
        return mutex.withLock {
            val refreshToken = tokenStorage.getToken()
            if (refreshToken != null && refreshToken != refresh) return@withLock true

            val newTokens = withTimeoutOrNull(timeoutMs) {
                refreshCall(refresh)
            } ?: return@withLock false

            if (newTokens.isBlank()) return@withLock false

            tokenStorage.saveRefreshToken(newTokens)
            true
        }
    }
}
