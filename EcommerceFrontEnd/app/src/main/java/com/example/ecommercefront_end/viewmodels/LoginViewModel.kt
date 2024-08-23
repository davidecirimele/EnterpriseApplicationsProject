package com.example.ecommercefront_end.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Credential
import com.example.ecommercefront_end.repository.AuthRepository

class LoginViewModel(private val loginRepository: AuthRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<Map<String, String>>()
    val loginResponse: LiveData<Map<String, String>> get() = _loginResponse

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> get() = _loginError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun login(credentials: Credential, onLoginSuccess: () -> Unit) {
        _isLoading.value = true


        try {
            Log.d(TAG, "login: tentativo $credentials")
            val response = loginRepository.loginUser(credentials)
            Log.d(TAG, "response: $response")
            if (response.isSuccessful && response.body() != null) {
                _loginResponse.value = response.body()
                response.body()?.let {
                    _loginResponse.value?.get("access_token")?.let { it1 -> SessionManager.saveAuthToken(it1) }
                    _loginResponse.value?.get("refresh_token")?.let { it2 -> SessionManager.saveRefreshToken(it2) }
                }
                onLoginSuccess()
            } else {
                _loginError.value = "Login failed: ${response.message()}"
            }
        } catch (e: Exception) {
            _loginError.value = "An error occurred: ${e.message}"
            Log.e(TAG, "An error occurred during login: ${e.localizedMessage}", e)
            _loginError.value = "An error occurred: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
}