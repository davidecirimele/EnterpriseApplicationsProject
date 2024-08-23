package com.example.ecommercefront_end

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.auth0.android.jwt.JWT
import com.example.ecommercefront_end.model.User
import com.google.gson.Gson
import java.time.LocalDate
import java.util.UUID
import kotlin.math.log

object SessionManager {

    private const val PREFS_NAME = "user_session"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val REFRESH_TOKEN_KEY  = "refresh_token"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    var user: User? = null
    private set

    var authToken: String? = null
    private set

    var refreshToken: String? = null
    private set

    fun init(context: Context){
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        prefs = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        loadSession()

    }

    private fun getPrefs(): SharedPreferences {
        return prefs ?: throw IllegalStateException("SessionManager not initialized. Call init() first.")
    }

    private fun loadSession(){
        authToken = getPrefs().getString(KEY_AUTH_TOKEN, null)
        refreshToken = getPrefs().getString(REFRESH_TOKEN_KEY, null)
    }

    fun saveAuthToken(token: String){
        authToken = token
        getPrefs().edit().putString(KEY_AUTH_TOKEN, token).apply()
        user = decodeJwtToken(token)
        Log.d(TAG, "user: ${user?.firstName}, ${user?.lastName}")
    }

    fun saveRefreshToken(token: String){
        refreshToken = token
        getPrefs().edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    fun clearSession(){
        getPrefs().edit().clear().apply()
        authToken = null
        refreshToken = null
        user = null
    }

    private fun decodeJwtToken(token: String): User? {
        Log.d(TAG, "decodeJwtToken: $token")
        val jwt = JWT(token)
        val userIdS = jwt.getClaim("userId").asString()
        val email = jwt.getClaim("sub").asString()
        val firstName = jwt.getClaim("firstName").asString()
        val lastName = jwt.getClaim("lastName").asString()
        val birthdate = jwt.getClaim("birthdate").asString()
        val phone_number = jwt.getClaim("phonenumber").asString()

        return if (userIdS != null && email != null && firstName != null && lastName != null && birthdate != null && phone_number != null) {
            val userId = UUID.fromString(userIdS)
            User(userId, firstName, lastName, LocalDate.parse(birthdate), phone_number)
        } else {
            null
        }

    }

    fun isLoggedIn(): Boolean {
        return user != null
    }











}