package com.example.inrussian.di

import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import com.example.inrussian.platformInterfaces.UserConfigurationStorageImpl
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun iosPlatformModule(
    userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
): Module = module {
    single<UserConfigurationStorage> { UserConfigurationStorageImpl(userDefaults) }
}

fun startKoinIos(
    userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
) {
    SharedDI.start(iosPlatformModule(userDefaults))
}