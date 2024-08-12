package com.example.ecommercefront_end.model

import java.time.LocalDate

data class Order(

    val id: Long,
    val user: User,
    val items: List<OrderItem>,
    val totalPrice: Double,
    //val shippingAddress: Address,
    //val status: OrderStatus,
    val paymentMethod: PaymentMethod,
    val orderDate : LocalDate? = null
)