package com.example.inrussian

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    fun doInitKoin() {
        startKoin {
            modules(allModules)
        }
    }
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()