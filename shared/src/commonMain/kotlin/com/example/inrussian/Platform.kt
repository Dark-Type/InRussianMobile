package com.example.inrussian

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform