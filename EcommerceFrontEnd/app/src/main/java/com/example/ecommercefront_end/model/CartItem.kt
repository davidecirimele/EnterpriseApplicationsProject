package com.example.ecommercefront_end.model

data class CartItem(
    val id: Long,
    val cartId : Long,
    val bookId : BookCartDto,
    var quantity: Int,

    val price: Double
)
