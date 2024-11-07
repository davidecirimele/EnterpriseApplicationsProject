package com.example.ecommercefront_end.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Transaction
import com.example.ecommercefront_end.repository.TransactionRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> get() = _transactions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val snackbarHostState = SnackbarHostState()

     fun fetchTransactions() {
        viewModelScope.launch {
            val userId = SessionManager.user?.id
            _isLoading.value = true
            try {
                val response = userId?.let { transactionRepository.getTransactions(it) }
                if ( response != null && response.isSuccessful) {
                    _transactions.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading transactions: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun onLogout(){
        //viewModelScope.cancel()
        _transactions.value = emptyList()
    }
}
