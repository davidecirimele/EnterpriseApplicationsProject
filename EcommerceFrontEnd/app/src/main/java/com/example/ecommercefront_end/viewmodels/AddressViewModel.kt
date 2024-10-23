package com.example.ecommercefront_end.viewmodels

import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.repository.AddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddressViewModel(private val repository: AddressRepository): ViewModel() {
    private val _addresses = MutableStateFlow<List<Address>?>(null)
    val addresses: StateFlow<List<Address>?> = _addresses

    private val _defaultAddress = MutableStateFlow<Address?>(null)
    val defaultAddress: StateFlow<Address?> = _defaultAddress

    private val _addressToEdit = MutableStateFlow<Address?>(null)
    val addressToEdit: StateFlow<Address?> = _addressToEdit

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    suspend fun fetchUserAddresses(userId: UUID? = null){
        try {
            if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                _addresses.value = repository.getAddresses(SessionManager.user!!.id)
            }else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN" && userId != null){
                _addresses.value = repository.getAddresses(userId)
            }
            else{
                Log.d("UserDebug", "User is null")
            }
        }catch (e: Exception){
            Log.e("AddressError", "Error loading default address", e)
        }
    }

    suspend fun fetchAddressById(userId: UUID? = null, addressId: Long){
        try {
            Log.d("AddressViewModel", "IM IN FABID")
            if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                _addressToEdit.value = repository.getAddress(SessionManager.user!!.id, addressId)
                Log.d("AddressViewModel", "Address fetched: ${_addressToEdit.value}")
            } else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN" && userId != null){
                _addressToEdit.value = repository.getAddress(userId, addressId)
            }
            else{
                Log.e("AddressViewModel", "SessionManager.user is null")
            }
        }catch(e : Exception){
            Log.d("UserDebug", "loadDefaultAddress: ${_addressToEdit.value}")
        }finally {
            _isLoading.value = false // Imposta isLoading a false dopo il caricamento, indipendentemente dal risultato
        }
    }

    suspend fun fetchDefaultAddress(userId: UUID? = null){
        Log.d("AddressViewModel", "fetchDefaultAddress called")
        try {
            if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                _defaultAddress.value = repository.getDefaultAddress(SessionManager.user!!.id)
                Log.d("AddressViewModel", "Default address fetched: ${_defaultAddress.value}")
            }else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN" && userId != null){
                _defaultAddress.value = repository.getDefaultAddress(userId)
                Log.d("AddressViewModel", "Default address fetched: ${_defaultAddress.value}")
            }
            else {
                _defaultAddress.value = null
                Log.e("AddressViewModel", "SessionManager.user is null")
            }
        }catch(e : Exception){
            _defaultAddress.value = null
            Log.d("UserDebug", "loadDefaultAddress: ${_defaultAddress.value}")
        }
    }

    fun setDefaultAddress(userId: UUID? = null,address: Address){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                    repository.updateDefaultAddress(UserId(SessionManager.user!!.id), address.id)
                    Log.d("AddressViewModel", "Default address setted: ${_defaultAddress.value}")
                }else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN" && userId != null){
                    repository.updateDefaultAddress(UserId(userId), address.id)
                    Log.d("AddressViewModel", "Default address setted: ${_defaultAddress.value}")
                }
                else{
                    Log.d("UserDebug", "User is null")
                    return@launch
                }
                fetchDefaultAddress(userId)
            } catch (e: Exception) {
                Log.d("UserDebug", "Error updating default address")
            }
        }
    }

    fun removeAddress(userId: UUID? = null, address: Address){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                    repository.deleteAddress(UserId(SessionManager.user!!.id), address.id)
                }else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN" && userId != null){
                    repository.deleteAddress(UserId(userId), address.id)
                }
                else{
                    Log.d("UserDebug", "User is null")
                    return@launch
                }
                fetchUserAddresses(userId)
                fetchDefaultAddress(userId)
            } catch (e: Exception) {
                Log.d("UserDebug", "Error deleting address")
            }
        }
    }

    fun insertAddress(userId: UUID?=null, address: SaveAddress){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                    repository.insertAddress(SessionManager.user!!.id, address)
                }else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN" && userId != null){
                    repository.insertAddress(userId, address)
                }
                else{
                    Log.d("UserDebug", "User is null")
                    return@launch
                }

                fetchUserAddresses(userId)
                Log.d("AddressViewModel", "Address added: ${_addresses.value}")

            } catch (e: Exception) {
                Log.d("UserDebug", "Error adding address")
            }
        }
    }

    fun editAddress(userId: UUID?, addressId: Long, address: SaveAddress){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                    repository.editAddress(SessionManager.user!!.id, addressId, address)
                }else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN" && userId != null){
                    repository.editAddress(userId, addressId, address)
                }
                else{
                    Log.d("UserDebug", "User is null")
                    return@launch
                }

                fetchUserAddresses(userId)
            } catch (e: Exception) {
                Log.d("UserDebug", "Error editing address")
            }
        }
    }
}