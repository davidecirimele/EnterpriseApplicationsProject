package com.example.ecommercefront_end.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AdminViewModel(private val repository: AdminRepository): ViewModel()  {

    private val _users = MutableStateFlow<List<UserDetails>?>(emptyList())//user si riferisce a UserDetails
    val users: StateFlow<List<UserDetails>?> = _users

    private val _filteredUsers = MutableStateFlow<List<UserDetails>?>(emptyList())
    val filteredUsers: StateFlow<List<UserDetails>?> = _filteredUsers

    private val _userFlow = MutableStateFlow<UserDetails?>(null)
    val userFlow: StateFlow<UserDetails?> = _userFlow.asStateFlow()

    suspend fun fetchUsers(forceReload: Boolean = false){
        try {
            var response = repository.getAllUsers()

            if (response != null) {
                if(response.isSuccessful && response.body()!=null) {
                    _users.value = response.body()
                    _filteredUsers.value = response.body()
                }
            }
            else{
                throw Exception()
            }
        }catch (e: Exception){
            Log.e("AdminError", "Error loading users", e)
        }
    }

    fun loadUser(userId: UUID) {
        viewModelScope.launch {
            val user = _users.value?.find { it.id == userId }
            _userFlow.value = user
        }
    }

    fun filterUser(value : String? = null){
        viewModelScope.launch {
            if (users.value != null) {
                try {
                    if (users.value!!.isNotEmpty()) {
                        _filteredUsers.value = users.value?.filter { user ->
                            (value == null ||
                                    user.firstName?.contains(value, ignoreCase = true) ?: false ||
                                    user.lastName?.contains(value, ignoreCase = true) ?: false ||
                                    user.id?.toString()?.contains(value, ignoreCase = true) ?: false ||
                                    user.email?.contains(value, ignoreCase = true) ?: false)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AdminError", "Error filtering users", e)
                }
            }
        }
    }

}