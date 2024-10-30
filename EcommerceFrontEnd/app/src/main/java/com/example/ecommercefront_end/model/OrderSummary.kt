package com.example.ecommercefront_end.model

data class OrderSummary(
    val orderId: Long,
    val email: String,
    val orderDate : String,
    val totalAmount : Double,
    val orderStatus : OrderStatus
)
