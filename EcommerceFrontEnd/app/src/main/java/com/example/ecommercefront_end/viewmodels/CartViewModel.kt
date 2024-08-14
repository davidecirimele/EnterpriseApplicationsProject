package com.example.ecommercefront_end.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.ecommercefront_end.model.CartItem
import com.example.ecommercefront_end.repository.CartRepository

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            try {
                val cart = repository.getCart()
                _cartItems.value = cart.items
                updateTotalAmount()
            } catch (e: Exception) {
                // Gestire l'errore, ad esempio mostrando un messaggio all'utente
            }
        }
    }

    fun updateItemQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            try {
                val updatedItem = item.copy(quantity = newQuantity)
                repository.updateQuantity(updatedItem.id, updatedItem.)
                // Ricarica gli articoli dopo l'aggiornamento
                _cartItems.value = _cartItems.value.map { currentItem ->
                    if (currentItem.id == updatedItem.id) updatedItem else currentItem
                }
                updateTotalAmount()

            } catch (e: Exception) {
                // Gestire l'errore
            }
        }
    }

    fun removeItem(item: CartItem) {
        // Implementare la logica di rimozione se necessario
        _cartItems.value = _cartItems.value.filterNot { it.id == item.id }
        updateTotalAmount()
    }

    private fun updateTotalAmount() {
        _totalAmount.value = _cartItems.value.sumOf { it.book.price * it.quantity }
    }
}