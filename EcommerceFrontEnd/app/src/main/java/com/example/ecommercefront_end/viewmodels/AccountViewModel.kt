package com.example.ecommercefront_end.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.Order
import com.example.ecommercefront_end.model.OrderSummary
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

    private val _userOrders = MutableStateFlow<List<OrderSummary>>(emptyList())
    val userOrders: StateFlow<List<OrderSummary>> get() = _userOrders

    private val _onError = MutableLiveData<String>()
    val onError: LiveData<String> get() = _onError

    private val _isLoadingOrders = MutableStateFlow(false)
    val isLoadingOrders: StateFlow<Boolean> get() = _isLoadingOrders

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

    fun fetchOrders() {
        viewModelScope.launch {
            _isLoadingOrders.value = true
            try {
                val user = SessionManager.user

                if(user != null && user.role != "ROLE_ADMIN") {
                    val response = repository.getUserOrders(user.id)
                    if (response.isSuccessful) {
                        val responseBody = response.body() as? List<OrderSummary>
                        if (responseBody != null) {
                            _userOrders.value = responseBody
                        }
                        else{
                            throw Exception("Response is null")
                        }
                    } else {
                        Log.e("AdminError", "Errore nella risposta: ${response?.code()}")
                    }
                } else{
                    throw Exception("User is null")
                }
            } catch (e: Exception) {
                Log.e("AdminError", "Errore durante il caricamento degli ordini", e)
            } finally {
                _isLoadingOrders.value = false
            }
        }
    }

}