package com.example.ecommercefront_end.model

import java.time.LocalDate

data class SaveOrder(

    val id : Long,
    val userId : UserId,
    val address : Address,
    val date : LocalDate,
    val totalAmount : Double,
    val paymentMethodId : PaymentMethodId,
    val orderItems : List<SaveOrderItem>
)
