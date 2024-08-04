package com.example.ecommercefront_end.model

data class CartItem(
    val id: Long,
    val  cartId : Long,
    val book : Book,
    val quantity: Int,
    val totalPrice: Double
)
