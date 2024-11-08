package com.example.ecommercefront_end.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.OrderSummary
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.repository.AdminRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.UUID

class AdminViewModel(private val repository: AdminRepository): ViewModel()  {

    private val _users = MutableStateFlow<List<UserDetails>>(emptyList())//user si riferisce a UserDetails
    val users: StateFlow<List<UserDetails>> = _users

    private val _filteredUsers = MutableStateFlow<List<UserDetails>?>(emptyList())
    val filteredUsers: StateFlow<List<UserDetails>?> = _filteredUsers

    private val _userFlow = MutableStateFlow<UserDetails?>(null)
    val userFlow: StateFlow<UserDetails?> = _userFlow.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderSummary>>(emptyList())
    val orders: StateFlow<List<OrderSummary>> get() = _orders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val snackbarHostState = SnackbarHostState()

    var currentPage = 0
    var totalPages = 1

    suspend fun fetchUsers(){
        try {
            var response = repository.getAllUsers()

            if (response != null) {
                if(response.isSuccessful && response.body()!=null) {
                    _users.value = response.body()!!
                    _filteredUsers.value = response.body()
                }
            }
            else{
                _errorMessage.value = "Error while fetching users."
            }
        }catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching users: Connection problem."
            else
                _errorMessage.value = "Error while fetching users."
        }
    }

    fun loadUser(userId: UUID) {
        viewModelScope.launch {
            if(users.value.isNotEmpty()) {
                val user = _users.value.find { it.id == userId }
                _userFlow.value = user
            }
            else{
                _errorMessage.value = "Error while loading user."
            }
        }
    }

    fun filterUser(value : String? = null){
        viewModelScope.launch {
            try {
                if (users.value.isNotEmpty()) {
                    _filteredUsers.value = users.value.filter { user ->
                        (value == null ||
                                user.firstName.contains(value, ignoreCase = true) ?: false ||
                                user.lastName.contains(value, ignoreCase = true) ?: false ||
                                user.id.toString().contains(value, ignoreCase = true) ?: false ||
                                user.email.contains(value, ignoreCase = true) ?: false)
                    }
                }
                else{
                    _errorMessage.value = "Error while filtering users."
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while filtering users: Connection problem."
                else
                    _errorMessage.value = "Error while filtering users."
            }
        }
    }

    fun fetchOrders(page: Int = 0) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getAllOrders(page, 10)
                if (response != null && response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        _orders.value = responseBody.content
                        currentPage = page
                        totalPages = responseBody.totalPages
                    } ?: run {
                        _errorMessage.value = "Error while fetching orders."
                    }
                } else {
                    _errorMessage.value = "Error while fetching orders."
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while fetching orders: Connection problem."
                else
                    _errorMessage.value = "Error while fetching orders."
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun loadNextPage() {
        if (currentPage + 1 < totalPages) {
            fetchOrders(currentPage + 1)
        }
    }

    fun loadPreviousPage() {
        if (currentPage > 0) {
            fetchOrders(currentPage - 1)
        }
    }

    fun onSnackbarDismissed(){
        _errorMessage.value = null
    }

    fun onLogout(){
        //viewModelScope.cancel()
        _users.value = emptyList()
        _filteredUsers.value = emptyList()
        _orders.value = emptyList()
        _userFlow.value = null
    }

}