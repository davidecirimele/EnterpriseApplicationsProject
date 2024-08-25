package com.example.ecommercefront_end.viewmodels

import android.util.Log
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
import java.util.UUID

class AccountViewModel(private val repository: AccountRepository): ViewModel() {
    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

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



    suspend fun editEmail(email : String, onSuccess : ()-> Unit){
        try {
            userDetails.value?.let { repository.editEmail(it.id, email) }
            onSuccess()
        }catch (e: Exception) {
            // TODO gestire eccezione
        }
    }

    suspend fun editPhoneNumber(phoneNumber : String, onSuccess : ()-> Unit){
        try {
            userDetails.value?.let { repository.editPhoneNumber(it.id, phoneNumber) }
            onSuccess()
        }catch (e: Exception) {
            // TODO gestire eccezione
        }
    }

}