package com.example.ecommercefront_end.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.model.CartItem
import com.example.ecommercefront_end.model.ShippingAddress
import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.OrderSummary
import com.example.ecommercefront_end.repository.CheckoutRepository
import com.example.ecommercefront_end.ui.theme.checkout.CheckoutDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: CheckoutRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val items = repository.getCartItems()
                _uiState.value = _uiState.value.copy(cartItems = items, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }


    fun updateShippingAddress(address: ShippingAddress) {
        _uiState.value = _uiState.value.copy(shippingAddress = address)
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        _uiState.value = _uiState.value.copy(paymentMethod = method)
    }

    fun updateOrderSummary() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            orderSummary = OrderSummary(
                items = currentState.cartItems,
                shippingAddress = currentState.shippingAddress,
                paymentMethod = currentState.paymentMethod,
                total = calculateTotal(currentState.cartItems)
            )
        )
    }

    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.price * it.quantity }
    }

    fun placeOrder() {
        viewModelScope.launch {
            val currentState = _uiState.value
            currentState.orderSummary?.let { order ->
                repository.placeOrder(order)
                _uiState.value = CheckoutUiState() // Reset the state
            }
        }
    }
}

data class CheckoutUiState(
    val cartItems: List<CartItem> = emptyList(),
    val shippingAddress: ShippingAddress = ShippingAddress(),
    val paymentMethod: PaymentMethod? = null,
    val orderSummary: OrderSummary? = null
)