package com.example.ecommercefront_end.model

data class SavePaymentMethod(

        val user : UserId,
        val cardHolderName : String,
        val paymentMethodType : Enum<PaymentMethodType>,
        val provider : Enum <CardProvider>,
        val cardNumber: String,
        val expirationDate: String

)
