package com.example.ecommercefront_end

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.auth0.android.jwt.JWT
import com.example.ecommercefront_end.model.User
import com.google.gson.Gson
import java.util.UUID

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

    private fun loadSession(){
        authToken = prefs.getString(KEY_AUTH_TOKEN, null)
        refreshToken = prefs.getString(REFRESH_TOKEN_KEY, null)
    }

    fun saveAuthToken(token: String){
        authToken = token
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
        user = decodeJwtToken(token)
    }

    fun saveRefreshToken(token: String){
        refreshToken = token
        prefs.edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    fun clearSession(){
        prefs.edit().clear().apply()
        authToken = null
        refreshToken = null
        user = null
    }

    private fun decodeJwtToken(token: String): User? {
        val jwt = JWT(token)
        val userIdS = jwt.getClaim("userId").asString()
        val email = jwt.getClaim("sub").asString()
        val firstName = jwt.getClaim("firstname").asString()
        val lastName = jwt.getClaim("lastname").asString()


        return if (userIdS != null && email != null && firstName != null && lastName != null) {
            val userId = UUID.fromString(userIdS)
            //User(userId, firstName, lastName)
            null
        } else {
            null
        }

    }



}