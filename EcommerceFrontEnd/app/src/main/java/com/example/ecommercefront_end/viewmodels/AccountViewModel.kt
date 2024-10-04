package com.example.ecommercefront_end.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.UUID

class AccountViewModel(private val repository: AccountRepository): ViewModel() {
    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    private val _onError = MutableLiveData<String>()
    val onError: LiveData<String> get() = _onError

    fun loadUserDetails(forceReload : Boolean = false) {

        if (_userDetails.value != null && !forceReload) {
            return
        }

        viewModelScope.launch {
            try {
                if (SessionManager.user != null) {
                    _userDetails.value = repository.getLoggedInUser(SessionManager.user!!.id)
                    Log.d("UserDebug", "loadUserDetails: ${_userDetails.value}")
                }
            } catch (e: Exception) {
                // Gestisci eccezione
                Log.e("UserDebug", "Errore durante il caricamento dei dati", e)
            }
        }
    }

    suspend fun editFunction(key:String, value : String, onSuccess : ()-> Unit, onError : ()-> Unit){
        viewModelScope.launch {
            try {
                val response: Response<User>?;

                if(key == "Email") {
                    response = userDetails.value?.let { repository.editEmail(it.id, value) }
                }
                else if(key == "Phone Number"){
                    response = userDetails.value?.let { repository.editPhoneNumber(it.id, value) }
                }
                else{
                    response = null
                }

                if (response != null) {
                    if (response.isSuccessful && response.body() != null) {
                        onSuccess()
                    }
                    else {
                        onError()
                    }
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

}