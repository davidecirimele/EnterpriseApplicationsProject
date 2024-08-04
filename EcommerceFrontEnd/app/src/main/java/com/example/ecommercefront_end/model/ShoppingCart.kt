package com.example.ecommercefront_end.model

data class ShoppingCart(
    val id: Long,
    val user: UserId,
    val items: List<CartItem>,
    val totalPrice: Double
)
