package com.example.ecommercefront_end.model

data class CheckoutRequest(
    val userId : UserId,
    val address : Address,
    val paymentMethodId : PaymentMethodId
)
