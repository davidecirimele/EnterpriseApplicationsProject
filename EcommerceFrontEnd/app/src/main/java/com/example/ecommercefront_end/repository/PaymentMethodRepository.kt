package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.SavePaymentMethod
import com.example.ecommercefront_end.network.PaymentMethodApiService

class PaymentMethodRepository( private val paymentMethodApiService: PaymentMethodApiService) {

    suspend fun addPaymentMethod(paymentMethod: SavePaymentMethod): PaymentMethod? {
        val response = paymentMethodApiService.addPaymentMethod(paymentMethod)
        if(response.isSuccessful){
            return response.body()
        } else {
            throw Exception("Error adding payment method")
        }
    }
}