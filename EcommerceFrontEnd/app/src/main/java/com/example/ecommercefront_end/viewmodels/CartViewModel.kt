package com.example.ecommercefront_end.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.ecommercefront_end.model.CartItem
import com.example.ecommercefront_end.model.ShoppingCart
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.net.SocketTimeoutException
import java.util.UUID

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _shoppingCart = MutableStateFlow<ShoppingCart?>(null)
    val shoppingCart: StateFlow<ShoppingCart?> = _shoppingCart

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val snackbarHostState = SnackbarHostState()

    val isCheckoutEnabled: StateFlow<Boolean> = cartItems.map { it.isNotEmpty() }.stateIn(viewModelScope, SharingStarted.Eagerly, false)







    fun loadCartItems() {
        viewModelScope.launch {

            if (_isLoading.value) {
                _isLoading.value = true
            } else {
                _isRefreshing.value = true
            }

            try {
                println("sto caricando il carrello")

                val cart = SessionManager.user?.let { repository.getCart(it.id) }

                if (cart != null) {
                    cart.onSuccess { cart_ ->
                        if (cart_ != null) {
                            _shoppingCart.value = cart_
                            _cartItems.value = cart_.cartItems
                        }
                        updateTotalAmount()
                    }.onFailure { e ->
                        if (e is SocketTimeoutException)
                            _errorMessage.value = "Errore: Si è verificato un problema di connessione."
                        else
                            _errorMessage.value = "Errore: ${e.message}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Errore durante il caricamento del carrello: ${e.message}"
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun updateItemQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            try {
                val updatedItem = item.copy(quantity = newQuantity)
                SessionManager.user?.let { UserId(it.id) }?.let {
                    shoppingCart.value?.let { it1 ->
                        repository.updateQuantity(updatedItem.quantity,
                            it.userId, it1.id, item.id
                        )
                    }
                }
                // Ricarica gli articoli dopo l'aggiornamento
                _cartItems.value = _cartItems.value.map { currentItem ->
                    if (currentItem.id == updatedItem.id) updatedItem else currentItem
                }
                updateTotalAmount()

            } catch (e: Exception) {
                _errorMessage.value = "Si è verificato  un errore durante l'aggiornamento della quantità: ${e.message}"
            }
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            try {
                val userId = SessionManager.user?.id
                if (userId != null) {

                    shoppingCart.value?.let {
                        repository.removeItem(item.id, it.id, userId)
                        println("shpping cart items post delete: ${_cartItems.value}")
                        _cartItems.value = _cartItems.value.filterNot { it.id == item.id }.toList()
                        println("Lista aggiornata dopo rimozione: ${_cartItems.value}")
                        updateTotalAmount()

                    }


                }

            } catch (e: Exception) {
                _errorMessage.value = "Si è verificato un errore durante la rimozione dell'articolo: ${e.message}"
            }
        }
    }

    private fun updateTotalAmount() {
        _totalAmount.value = _cartItems.value.sumOf { it.bookId.price * it.quantity }
    }
}
