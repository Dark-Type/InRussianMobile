package com.example.inrussian.platformInterfaces
import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.inrussian.navigation.configurations.Configuration
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.inrussian.components.main.profile.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserConfigurationStorageImpl(private val context: Context) : UserConfigurationStorage {
    private val Context.dataStore by preferencesDataStore(name = "config_storage")

    private val key = stringPreferencesKey("last_configuration")
    private val tokenKey = stringPreferencesKey("user_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    private val notificationsKey = stringPreferencesKey("notifications_enabled")
    private val themeKey = stringPreferencesKey("app_theme")

    // --------------------------
    // App configuration (DataStore)
    // --------------------------
    override fun save(configuration: Configuration) {
        runBlocking {
            context.dataStore.edit { prefs: MutablePreferences ->
                prefs[key] = when (configuration) {
                    Configuration.Auth -> "Auth"
                    Configuration.Onboarding -> "Onboarding"
                    Configuration.Main -> "Main"
                }
            }
        }
    }

    override fun get(): Configuration? {
        return runBlocking {
            val prefs = context.dataStore.data.first()
            when (prefs[key]) {
                "Auth" -> Configuration.Auth
                "Onboarding" -> Configuration.Onboarding
                "Main" -> Configuration.Main
                else -> null
            }
        }
    }

    // --------------------------
    // Tokens (DataStore)
    // --------------------------
    override fun saveToken(token: String) {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[tokenKey] = token
            }
        }
    }

    override fun getToken(): String? {
        return runBlocking {
            val prefs = context.dataStore.data.first()
            prefs[tokenKey]
        }
    }

    override fun deleteToken() {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs.remove(tokenKey)
            }
        }
    }

    override fun saveRefreshToken(token: String) {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[refreshTokenKey] = token
            }
        }
    }

    override fun getRefreshToken(): String? {
        return runBlocking {
            val prefs = context.dataStore.data.first()
            prefs[refreshTokenKey]
        }
    }

    override fun deleteRefreshToken() {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs.remove(refreshTokenKey)
            }
        }
    }

    // --------------------------
    // Credentials (EncryptedSharedPreferences)
    // --------------------------
    private fun getEncryptedPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    override fun saveCreds(creds: Creds) {
        val prefs = getEncryptedPrefs(context)
        when (creds) {
            is Creds.Email -> prefs.edit().putString("user_email", creds.email).apply()
            is Creds.Password -> prefs.edit().putString("user_password", creds.password).apply()
        }
    }

    override fun getCreds(): Creds? {
        val prefs = getEncryptedPrefs(context)
        val emailVal = prefs.getString("user_email", null)
        val passwordVal = prefs.getString("user_password", null)
        return when {
            emailVal != null -> Creds.Email(emailVal)
            passwordVal != null -> Creds.Password(passwordVal)
            else -> null
        }
    }

    override fun deleteCreds() {
        val prefs = getEncryptedPrefs(context)
        prefs.edit().remove("user_email").remove("user_password").apply()
    }
    override fun saveNotificationsEnabled(enabled: Boolean){
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[notificationsKey] = enabled.toString()
            }
        }
    }
    override fun getNotificationsEnabled(): Boolean? {
        return runBlocking {
            val prefs = context.dataStore.data.first()
            prefs[notificationsKey]?.toBoolean()
        }
    }
    override fun saveTheme(theme: AppTheme){
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[themeKey] = when(theme){
                    AppTheme.SYSTEM -> "SYSTEM"
                    AppTheme.LIGHT -> "LIGHT"
                    AppTheme.DARK -> "DARK"
                }
            }
        }
    }
    override fun getTheme(): AppTheme?{
        return runBlocking {
            val prefs = context.dataStore.data.first()
            when(prefs[themeKey]){
                "SYSTEM" -> AppTheme.SYSTEM
                "LIGHT" -> AppTheme.LIGHT
                "DARK" -> AppTheme.DARK
                else -> null
            }
        }
    }
}