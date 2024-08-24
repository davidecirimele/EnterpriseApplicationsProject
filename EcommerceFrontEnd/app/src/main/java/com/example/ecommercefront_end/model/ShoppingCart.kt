package com.example.ecommercefront_end.model

data class ShoppingCart(
    val id: Long,
    val userId: UserId,
    val cartItems: List<CartItem>,
    )
