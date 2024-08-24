package com.example.ecommercefront_end.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AccountViewModel(private val repository: AccountRepository): ViewModel() {
    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails


    init {
        try{
        viewModelScope.launch {
            //loadUserDetails()
        }} catch (e: Exception) {
            // TODO gestire eccezione
        }
    }

    private suspend fun loadUserDetails(){

        _userDetails.value = SessionManager.user?.id?.let { repository.getLoggedInUser(it) }

    }

}