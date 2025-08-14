package com.example.inrussian

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    val test: String = "YES"
}

actual fun getPlatform(): Platform = IOSPlatform()