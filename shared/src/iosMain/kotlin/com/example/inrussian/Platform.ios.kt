package com.example.inrussian

import platform.UIKit.UIDevice
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    val test: String = "YES"
}



fun <T : Any> StateFlow<T>.watchState(onEach: (T) -> Unit): Job {
    val scope = CoroutineScope(Dispatchers.Main)
    return scope.launch {
        collect { onEach(it) }
    }
}

actual fun getPlatform(): Platform = IOSPlatform()