package com.example.ecommercefront_end.viewmodels

import android.se.omapi.Session
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.utils.ErrorMessageParser
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.util.UUID

class AddressViewModel(private val repository: AddressRepository): ViewModel() {
    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses

    private val _defaultAddress = MutableStateFlow<Address?>(null)
    val defaultAddress: StateFlow<Address?> = _defaultAddress

    private val _addressToEdit = MutableStateFlow<Address?>(null)
    val addressToEdit: StateFlow<Address?> = _addressToEdit

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val snackbarHostState = SnackbarHostState()


    suspend fun fetchUserAddresses(userId: UUID? = null){
        try {
            var user = SessionManager.user!!.id

            if(userId != null && SessionManager.user!!.role == "ROLE_ADMIN")
                user = userId

            val response = repository.getAddresses(user)

            if(response.isSuccessful && !response.body().isNullOrEmpty()) {
                _addresses.value = response.body()!!
            }
            else{
                _addresses.value = emptyList()
            }
        }catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching the addresses: Connection problem."
            else
                _errorMessage.value = "Error while fetching the address."
        }
    }

    suspend fun fetchAddressById(userId: UUID? = null, addressId: Long){
        try {
            var user = SessionManager.user!!.id

            if(userId != null && SessionManager.user!!.role == "ROLE_ADMIN")
                user = userId

            val response = repository.getAddress(user, addressId)

            if (response.isSuccessful && response.body() != null) {
                _addressToEdit.value = response.body()
            }
        }catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching the address: Connection problem."
            else
                _errorMessage.value = "Error while fetching the address."
        }finally {
            _isLoading.value = false // Imposta isLoading a false dopo il caricamento, indipendentemente dal risultato
        }
    }

    suspend fun fetchDefaultAddress(userId: UUID? = null){
        if(addresses.value.isNotEmpty())
        {
            try {
                var user = SessionManager.user!!.id

                if(userId != null && SessionManager.user!!.role == "ROLE_ADMIN")
                    user = userId

                val response = repository.getDefaultAddress(user)

                if (response.isSuccessful && response.body()!= null) {
                    _defaultAddress.value = response.body()
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while fetching the default address: Connection problem."
                else
                    _errorMessage.value = "Error while fetching the default address."
            }
        }
    }

    fun setDefaultAddress(userId: UUID? = null,address: Address){
        viewModelScope.launch {
            try {
                var user = SessionManager.user!!.id

                if(userId != null && SessionManager.user!!.role == "ROLE_ADMIN")
                    user = userId

                val response = repository.updateDefaultAddress(user, address.id)

                if (response.isSuccessful && response.body() != null) {
                    fetchDefaultAddress(user)
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while updating the default address: Connection problem."
                else
                    _errorMessage.value = "Error while updating the default address."
            }
        }
    }

    fun removeAddress(userId: UUID? = null, address: Address){
        viewModelScope.launch {
            try {
                var user = SessionManager.user!!.id

                if(userId != null && SessionManager.user!!.role == "ROLE_ADMIN")
                    user = userId

                repository.deleteAddress(user, address.id)

                fetchUserAddresses(user)
                fetchDefaultAddress(user)
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while deleting the address: Connection problem."
                else
                    _errorMessage.value = "Error while deleting the address."
            }
        }
    }

    fun insertAddress(userId: UUID?=null, address: SaveAddress, onSuccess: ()->Unit){
        viewModelScope.launch {
            try {
                var user = SessionManager.user!!.id

                if(userId != null && SessionManager.user!!.role == "ROLE_ADMIN")
                    user = userId

                val response = repository.insertAddress(user, address)

                if (response.isSuccessful && response.body()!=null) {
                    fetchUserAddresses(user)
                    onSuccess()
                }
                else{
                    val errorBody = response.errorBody()?.string()

                    _errorMessage.value = ErrorMessageParser(errorBody)
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while saving the address: Connection problem."
                else
                    _errorMessage.value = "Error while saving the address."
            }
        }
    }

    fun editAddress(userId: UUID?, addressId: Long, address: SaveAddress, onSuccess: ()->Unit){
        viewModelScope.launch {
            try {
                var user = SessionManager.user!!.id

                if(userId != null && SessionManager.user!!.role == "ROLE_ADMIN")
                    user = userId

                val response = repository.editAddress(user, addressId, address)

                if (response.isSuccessful && response.body()!=null) {
                    fetchUserAddresses(user)
                    onSuccess()
                } else{
                    val errorBody = response.errorBody()?.string()

                    _errorMessage.value = ErrorMessageParser(errorBody)
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while saving the address: Connection problem."
                else
                    _errorMessage.value = "Error while saving the address."
            }
        }
    }

    fun onSnackbarDismissed(){
        _errorMessage.value = null
    }

    fun onLogout(){
        //viewModelScope.cancel()
        _addresses.value = emptyList()
        _defaultAddress.value = null
        _addressToEdit.value = null
    }
}