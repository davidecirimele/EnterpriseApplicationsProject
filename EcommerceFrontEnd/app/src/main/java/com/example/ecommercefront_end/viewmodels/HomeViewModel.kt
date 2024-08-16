package com.example.ecommercefront_end.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.CartItem
import com.example.ecommercefront_end.network.BooksService
import com.example.ecommercefront_end.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val booksService: BooksService) : ViewModel(){
    private val _products = MutableStateFlow<List<Book>>(emptyList())
    val products: StateFlow<List<Book>> = _products

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _hasError = mutableStateOf(false)
    val hasError: State<Boolean> = _hasError

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            try {
                _products.value = booksService.getBooks()
            } catch (e: Exception) {
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}


