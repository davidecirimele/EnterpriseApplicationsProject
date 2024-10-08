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


    suspend fun fetchUserAddresses(forceReload: Boolean = false){
        try {
            _addresses.value = SessionManager.user?.let { repository.getAddresses(it.id) }
        }catch (e: Exception){
            Log.e("AddressError", "Error loading default address", e)
        }
    }

    suspend fun fetchAddressById(addressId: Long){
        try {
            Log.d("AddressViewModel", "IM IN FABID")
            if (SessionManager.user != null) {
                Log.d("AddressViewModel", "STO PER PRENDERE INDIRIZZO BY ID")
                _addressToEdit.value = repository.getAddress(SessionManager.user!!.id, addressId)
                Log.d("AddressViewModel", "Address fetched: ${_addressToEdit.value}")
            } else {
                Log.e("AddressViewModel", "SessionManager.user is null")
            }
        }catch(e : Exception){
            Log.d("UserDebug", "loadDefaultAddress: ${_addressToEdit.value}")
        }finally {
            _isLoading.value = false // Imposta isLoading a false dopo il caricamento, indipendentemente dal risultato
        }
    }

    suspend fun fetchDefaultAddress(forceReload: Boolean = false){
        Log.d("AddressViewModel", "fetchDefaultAddress called")
        try {
            if (SessionManager.user != null) {
                _defaultAddress.value = repository.getDefaultAddress(SessionManager.user!!.id)
                Log.d("AddressViewModel", "Default address fetched: ${_defaultAddress.value}")
            } else {
                _defaultAddress.value = null
                Log.e("AddressViewModel", "SessionManager.user is null")
            }
        }catch(e : Exception){
            _defaultAddress.value = null
            Log.d("UserDebug", "loadDefaultAddress: ${_defaultAddress.value}")
        }
    }

    fun setDefaultAddress(address: Address){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null) {
                    repository.updateDefaultAddress(UserId(SessionManager.user!!.id), address.id)
                    fetchDefaultAddress()
                    Log.d("AddressViewModel", "Default address setted: ${_defaultAddress.value}")
                }
            } catch (e: Exception) {
                Log.d("UserDebug", "Error updating default address")
            }
        }
    }

    fun removeAddress(address: Address){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null) {
                    repository.deleteAddress(UserId(SessionManager.user!!.id), address.id)
                    fetchUserAddresses()
                    fetchDefaultAddress(true)
                    Log.d("AddressViewModel", "Address deleted: ${_addresses.value}")
                }
                else
                    Log.d("UserDebug", "User is null")
            } catch (e: Exception) {
                Log.d("UserDebug", "Error deleting address")
            }
        }
    }

    fun insertAddress(address: SaveAddress){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null) {
                    repository.insertAddress(SessionManager.user!!.id, address)
                    fetchUserAddresses()
                    Log.d("AddressViewModel", "Address added: ${_addresses.value}")
                }
                else
                    Log.d("UserDebug", "User is null")
            } catch (e: Exception) {
                Log.d("UserDebug", "Error adding address")
            }
        }
    }

    fun editAddress(addressId: Long, address: SaveAddress){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null) {
                    repository.editAddress(SessionManager.user!!.id, addressId, address)
                    fetchUserAddresses()
                    Log.d("AddressViewModel", "Address edited: ${_addresses.value}")
                }
                else
                    Log.d("UserDebug", "User is null")
            } catch (e: Exception) {
                Log.d("UserDebug", "Error editing address")
            }
        }
    }
}