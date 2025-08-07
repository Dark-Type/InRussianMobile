package com.example.inrussian

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android <<< iOS"
}

actual fun getPlatform(): Platform = AndroidPlatform()