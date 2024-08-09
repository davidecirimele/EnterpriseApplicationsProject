package com.example.ecommercefront_end.model

data class InsertCartItem(
    val cartId: Long,
    val userId: UserId,
    val bookId: BookId,
    val quantity: Int
)
