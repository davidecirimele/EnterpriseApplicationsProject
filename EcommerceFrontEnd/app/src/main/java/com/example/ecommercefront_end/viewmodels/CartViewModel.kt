package com.example.ecommercefront_end.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Book

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.ecommercefront_end.model.CartItem
import com.example.ecommercefront_end.model.ShoppingCart
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.repository.CartRepository
import kotlinx.coroutines.cancel
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

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String?>  get() = _errorMessage

    val snackbarHostState = SnackbarHostState()

    val isCheckoutEnabled: StateFlow<Boolean> = cartItems.map { it.isNotEmpty() }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar: StateFlow<Boolean> get() = _showSnackbar

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> get() = _snackbarMessage




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
                            _errorMessage.value = "Error: a connection timeout occurred: ${e.message}"
                        else
                            _errorMessage.value = "Error: ${e.message}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error during cart loading: ${e.message}"
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
        println("error message ${_errorMessage.value}")
        println("error message ${errorMessage}")
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
                _errorMessage.value = "An errror occurred while updating the quantity: ${e.message}"
            }
        }
    }

    fun addItem(book : Book) {
        viewModelScope.launch {
            try {
                val userId = SessionManager.user?.id
                if (userId != null) {
                    val response = repository.addCartItem(userId, 1, book.id)

                    if (response.isSuccessful) {
                        updateTotalAmount()
                            _errorMessage.value = "Book added to cart successfully"
                    }
                    else {
                        _errorMessage.value = "Error: ${response.message()}"
                    }
                } else {
                    _errorMessage.value = "User ID not found"
                }

            } catch (e: Exception) {
                _errorMessage.value = "An error occurred while adding the item to the cart."
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



                        _cartItems.value = _cartItems.value.filterNot { it.id == item.id }.toList()


                        updateTotalAmount()
                    }
                    _errorMessage.value = "Item removed successfully"
                } else {
                    _errorMessage.value = "User ID not found"

                }


            } catch (e: Exception) {
                _errorMessage.value = " An error occurred while removing the item."
            }
        }
    }

    private fun updateTotalAmount() {
        _totalAmount.value = _cartItems.value.sumOf { it.bookId.price * it.quantity }
    }

    fun setShowSnackbar(b: Boolean) {
        _showSnackbar.value = b

    }

    fun onLogout(){
        //viewModelScope.cancel()
        _cartItems.value = emptyList()
        _shoppingCart.value = null
        _totalAmount.value = 0.0
    }
}
