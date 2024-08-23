package com.example.ecommercefront_end.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.repository.AddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddressViewModel(private val repository: AddressRepository): ViewModel() {
    private val _addresses = MutableStateFlow<List<Address>?>(null)
    val addresses: StateFlow<List<Address>?> = _addresses

    init {
        try{
            viewModelScope.launch {
                loadUserAddresses()
            }} catch (e: Exception) {
            // TODO gestire eccezione
        }
    }

    private suspend fun loadUserAddresses(){

        _addresses.value = SessionManager.user?.let { repository.getAddresses(it.id) }

    }
}