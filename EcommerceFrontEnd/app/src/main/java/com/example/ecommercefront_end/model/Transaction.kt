package com.example.ecommercefront_end.model

import java.time.LocalDate

data class Transaction(
    val id: Long,
    val user: User,
    val order: Order,
    val paymentMethod: PaymentMethod,
    val amount : Double,
    val PaymentStatus : PaymentStatus,
    val transactionDate : LocalDate? = null
)
