package com.example.inrussian.data.client.auth

import io.ktor.util.encodeBase64
import com.example.inrussian.data.client.auth.HttpBasicAuth

class HttpBasicAuth : Authentication {
    var username: String? = null
    var password: String? = null

    override fun apply(query: MutableMap<String, List<String>>, headers: MutableMap<String, String>) {
        if (this.username == null && this.password == null) return
        val str = (this.username ?: "") + ":" + (this.password ?: "")
        val auth = str.encodeBase64()
        headers["Authorization"] = "Basic $auth"
    }
}
