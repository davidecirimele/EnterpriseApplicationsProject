package com.example.ecommercefront_end.model

data class OrderItem(
    val id: Long,
    val Order : Order?,
    val product: Product,
    val quantity: Int
)