package com.example.inrussian.di.main

import com.example.inrussian.repository.main.BadgeRepository
import com.example.inrussian.repository.main.MockBadgeRepository
import com.example.inrussian.repository.main.home.HomeRepository
import com.example.inrussian.repository.main.home.HomeRepositoryImpl
import com.example.inrussian.repository.main.settings.SettingsRepository
import com.example.inrussian.repository.main.settings.SettingsRepositoryImpl
import com.example.inrussian.repository.main.train.MockTrainRepository
import com.example.inrussian.repository.main.train.TrainRepository
import com.example.inrussian.repository.main.user.UserRepository
import com.example.inrussian.repository.main.user.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val QAppScope = named("AppScope")
val QAboutText = named("AboutText")
val QPrivacyText = named("PrivacyText")

val mainModule = module {
    single(QAppScope) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single<UserRepository> {
        UserRepositoryImpl(
            api = get(),
        )
    }
    single<BadgeRepository> { MockBadgeRepository(scope = get(QAppScope)) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

//    single<TrainRepository> {
//        val appScope: CoroutineScope = get(named("AppScope"))
//        TrainRepositoryImpl(get())
//    }
    single<TrainRepository> {
        MockTrainRepository()
    }
    single<HomeRepository> {
        HomeRepositoryImpl(get(), get(), get())
    }
    single(QAboutText) {
        """
        Это учебное приложение для демонстрации архитектуры Kotlin Multiplatform, Decompose и Koin.
        Версия: 1.0.0
        Основные функции: обучение, тренировки, профиль пользователя.
        """.trimIndent()
    }
    single(QPrivacyText) {
        """
        Политика конфиденциальности:
        Мы уважаем вашу приватность. Данные используются только в демонстрационных целях,
        не передаются третьим лицам и могут быть сброшены без уведомления.
        Используя приложение, вы соглашаетесь с обработкой данных в учебных целях.
        """.trimIndent()
    }
}