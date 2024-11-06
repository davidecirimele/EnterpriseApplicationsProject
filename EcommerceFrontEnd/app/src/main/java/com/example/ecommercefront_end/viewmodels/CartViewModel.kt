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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    //val snackbarHostState = SnackbarHostState()

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
                            _errorMessage.emit("Error: connection issue. Please check your internet connection")
                        else
                            _errorMessage.emit("Error: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error while loading cart: ${e.message}")
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
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
                _errorMessage.emit("An error occurred while updating the item.")

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
                            _errorMessage.emit(" Book added to cart.")
                    }
                    else {
                        _errorMessage.emit("Error: ${response.message()}")
                    }
                } else {
                    _errorMessage.emit("User ID not found.")
                }

            } catch (e: Exception) {
                _errorMessage.emit("An error occurred while adding the item.")
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
                    _errorMessage.emit("Item removed successfully.")
                } else {
                    _errorMessage.emit("User ID not found.")

                }


            } catch (e: Exception) {
                _errorMessage.emit(" An error occurred while removing the item.")
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
