package com.example.ecommercefront_end.model

data class PaymentMethod (

    val id: Long,
    val user : User,
    val cardHolderName : String,
    val paymentMethodType : Enum<PaymentMethodType>,
    val provider : Enum <CardProvider>,
    val cardNumber: String,
    val expirationDate: String
)

