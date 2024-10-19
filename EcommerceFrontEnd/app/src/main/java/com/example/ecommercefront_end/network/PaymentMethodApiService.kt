package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.SavePaymentMethod
import retrofit2.Response
import retrofit2.http.POST

interface PaymentMethodApiService {

    @POST("paymentMethods/add")
    suspend fun addPaymentMethod(paymentMethod: SavePaymentMethod) : Response<PaymentMethod?>
}