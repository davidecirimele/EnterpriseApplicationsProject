package com.example.ecommercefront_end.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.ecommercefront_end.model.CartItem
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.repository.CartRepository
import java.util.UUID

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount


    fun loadCartItems() {
        viewModelScope.launch {
            try {
                println("sto caricando il carrello")

                val cart = repository.getCart(getUser().id)
                cart.onSuccess { cart_ ->
                    if (cart_ != null) {
                        _cartItems.value = cart_.cartItems
                    }
                    updateTotalAmount()
                }.onFailure { e ->
                    println("Errore: ${e.message}")
                }
            } catch (e: Exception) {
                // Gestire l'errore, ad esempio mostrando un messaggio all'utente
            }
        }
    }

    fun updateItemQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            try {

                val updatedItem = item.copy(quantity = newQuantity)
                SessionManager.user?.let { UserId(it.id) }?.let {
                    repository.updateQuantity(updatedItem.quantity,
                        it
                    )
                }
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
        viewModelScope.launch {
            try {
                val userId = getUser().id
                if (userId != null)
                    repository.removeItem(item.id, UserId(userId))
                _cartItems.value = _cartItems.value.filterNot { it.id == item.id }

            } catch (e: Exception) {
                // Gestire l'errore
            }
        }
        updateTotalAmount()
    }

    private fun updateTotalAmount() {
        _totalAmount.value = _cartItems.value.sumOf { it.bookId.price * it.quantity }
    }


    private fun getUser(): UserId {
        val userId = SessionManager.user?.id
        println("UserId recuperato: $userId")
        return UserId(userId ?: throw IllegalStateException("User not logged in"))
    }
}
