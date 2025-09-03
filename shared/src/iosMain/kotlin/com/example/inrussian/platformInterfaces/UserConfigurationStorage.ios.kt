package com.example.inrussian.platformInterfaces

import com.example.inrussian.navigation.configurations.Configuration
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.NSUserDefaults
import platform.Security.*

class UserConfigurationStorageImpl(
    private val defaults: NSUserDefaults
) : UserConfigurationStorage {

    private val key = "last_configuration"
    private val tokenKey = "user_token"
    private val refreshTokenKey = "refresh_token"
    private val keychainService = "com.example.inrussian"

    // --------------------------
    // App configuration (NSUserDefaults)
    // --------------------------

    override fun save(configuration: Configuration) {
        val value = when (configuration) {
            Configuration.Auth -> "Auth"
            Configuration.Onboarding -> "Onboarding"
            Configuration.Main -> "Main"
        }
        defaults.setObject(value, forKey = key)
    }

    override fun get(): Configuration? {
        val stored = defaults.stringForKey(key)
        return when (stored) {
            "Auth" -> Configuration.Auth
            "Onboarding" -> Configuration.Onboarding
            "Main" -> Configuration.Main
            else -> Configuration.Auth
        }
    }

    // --------------------------
    // Tokens (NSUserDefaults)
    // --------------------------

    override fun saveToken(token: String) {
        defaults.setObject(token, forKey = tokenKey)
    }

    override fun getToken(): String? = defaults.stringForKey(tokenKey)
    override fun deleteToken() = defaults.removeObjectForKey(tokenKey)
    override fun saveRefreshToken(token: String) =
        defaults.setObject(token, forKey = refreshTokenKey)

    override fun getRefreshToken(): String? = defaults.stringForKey(refreshTokenKey)
    override fun deleteRefreshToken() = defaults.removeObjectForKey(refreshTokenKey)

    // --------------------------
    // Credentials (Keychain)
    // --------------------------

    @OptIn(ExperimentalForeignApi::class)
    override fun saveCreds(creds: Creds) {
        when (creds) {
            is Creds.Email -> setKeychainValue("user_email", creds.email)
            is Creds.Password -> setKeychainValue("user_password", creds.password)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun getCreds(): Creds? {
        val email = getKeychainValue("user_email")
        val password = getKeychainValue("user_password")
        return when {
            email != null -> Creds.Email(email)
            password != null -> Creds.Password(password)
            else -> null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun deleteCreds() {
        deleteKeychainValue("user_email")
        deleteKeychainValue("user_password")
    }

    // --------------------------
    // Keychain helpers (CoreFoundation dictionaries)
    // --------------------------

    @OptIn(ExperimentalForeignApi::class)
    private fun buildBaseQuery(accountKey: String): CFMutableDictionaryRef = memScoped {
        val dict = CFDictionaryCreateMutable(
            kCFAllocatorDefault, 0, null, null
        )!!
        CFDictionarySetValue(dict, kSecClass, kSecClassGenericPassword)
        CFDictionarySetValue(dict, kSecAttrAccount, accountKey.toCFString())
        CFDictionarySetValue(dict, kSecAttrService, keychainService.toCFString())
        dict
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setKeychainValue(accountKey: String, value: String) = memScoped {
        val query = buildBaseQuery(accountKey)
        val attrsToUpdate = CFDictionaryCreateMutable(kCFAllocatorDefault, 0, null, null)!!
        CFDictionarySetValue(attrsToUpdate, kSecValueData, value.encodeToByteArray().toCFData())
        val updateStatus = SecItemUpdate(query, attrsToUpdate)
        if (updateStatus == errSecItemNotFound) {
            CFDictionarySetValue(query, kSecValueData, value.encodeToByteArray().toCFData())
            SecItemAdd(query, null)
        }
        CFRelease(attrsToUpdate)
        CFRelease(query)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun getKeychainValue(accountKey: String): String? = memScoped {
        val dict = CFDictionaryCreateMutable(
            kCFAllocatorDefault, 0, null, null
        )!!
        CFDictionarySetValue(dict, kSecClass, kSecClassGenericPassword)
        CFDictionarySetValue(dict, kSecAttrAccount, accountKey.toCFString())
        CFDictionarySetValue(dict, kSecAttrService, keychainService.toCFString())
        CFDictionarySetValue(dict, kSecReturnData, kCFBooleanTrue)
        CFDictionarySetValue(dict, kSecMatchLimit, kSecMatchLimitOne)

        val result = alloc<CFTypeRefVar>()
        val status = SecItemCopyMatching(dict, result.ptr)
        CFRelease(dict)
        if (status == errSecSuccess && result.value != null) {
            result.value?.toByteArray()?.decodeToString()
        } else null
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun deleteKeychainValue(accountKey: String) = memScoped {
        val query = buildBaseQuery(accountKey)
        SecItemDelete(query)
        CFRelease(query)
    }

    // --------------------------
    // CF helpers
    // --------------------------

    @OptIn(ExperimentalForeignApi::class)
    private fun String.toCFString(): CFStringRef? = memScoped {
        CFStringCreateWithCString(
            kCFAllocatorDefault,
            this@toCFString.toString(),
            kCFStringEncodingUTF8
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun ByteArray.toCFData(): CFDataRef? = this.toUByteArray().usePinned { pinned ->
        CFDataCreate(
            kCFAllocatorDefault,
            pinned.addressOf(0),
            this.size.toLong()
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun COpaquePointer?.toByteArray(): ByteArray {
        if (this == null) return ByteArray(0)
        val cfDataRef = this as CFDataRef
        val length = CFDataGetLength(cfDataRef)
        val out = UByteArray(length.toInt())
        out.usePinned { pinned ->
            CFDataGetBytes(cfDataRef, CFRangeMake(0, length), pinned.addressOf(0))
        }
        return out.toByteArray()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun CFDataRef.toByteArray(): ByteArray {
        val length = CFDataGetLength(this)
        val out = UByteArray(length.toInt())
        out.usePinned { pinned ->
            CFDataGetBytes(this, CFRangeMake(0, length), pinned.addressOf(0))
        }
        return out.toByteArray()
    }
}
