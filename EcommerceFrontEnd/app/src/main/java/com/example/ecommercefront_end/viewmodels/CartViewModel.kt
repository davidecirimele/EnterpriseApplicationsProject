package com.example.ecommercefront_end.viewmodels

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _shoppingCart = MutableStateFlow<ShoppingCart?>(null)
    val shoppingCart: StateFlow<ShoppingCart?> = _shoppingCart

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    val isCheckoutEnabled: StateFlow<Boolean> = cartItems.map { cartItems ->
        println("cartItems: $cartItems")
        cartItems.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)





    fun loadCartItems() {
        viewModelScope.launch {

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
                        println("Errore: ${e.message}")
                    }
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
                // Gestire l'errore
            }
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            try {
                val userId = SessionManager.user?.id
                if (userId != null) {
                    println("shpping cart items pre delete: ${_cartItems.value}")
                    shoppingCart.value?.let {
                        repository.removeItem(item.id, it.id, userId)
                        println("shpping cart items post delete: ${_cartItems.value}")
                        _cartItems.value = _cartItems.value.filterNot { it.id == item.id }.toList()
                        println("Lista aggiornata dopo rimozione: ${_cartItems.value}")
                        updateTotalAmount()

                    }


                }

            } catch (e: Exception) {
                println()
            }
        }
    }


    private fun updateTotalAmount() {
        _totalAmount.value = _cartItems.value.sumOf { it.bookId.price * it.quantity }
    }
}
