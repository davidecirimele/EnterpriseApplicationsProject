package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.CheckoutRequest
import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveOrder
import com.example.ecommercefront_end.model.SavePaymentMethod
import com.example.ecommercefront_end.model.UserId
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CheckoutApiService {

    // Recupera l'indirizzo di spedizione dell'utente
    @GET("addresses/{id}/default")
    @RequiresAuth
    suspend fun getDefaultAddressByUserId(@Path("id") userId: UUID) : Response<Address?>

    // Aggiorna l'indirizzo di spedizione dell'utente
    @PUT("{userId}/{addressId}/update-default")
    suspend fun updateDefaultShippingAddress(
        @Path("addressId") addressId: Long,
        @Path("userId") userId: UUID
    ) : Address

    @PUT("addresses/{userId}/{addressId}/edit-address")
    @RequiresAuth
    suspend fun updateShippingAddress(
        @Path("userId") userId: UUID,
        @Path("addressId") addressId: Long,
        @Body address: SaveAddress
    ) : Address

    @GET("addresses/{id}")
    @RequiresAuth
    suspend fun getShippingAddresses(@Path("id") userId: UUID) : Response<List<Address>?>

    @POST("addresses/{userId}/insert-address")
    @RequiresAuth
    suspend fun insertShippingAddress(
        @Path("userId") userId: UUID,
        @Body address: SaveAddress
    ) : Address

    // Recupera la lista dei metodi di pagamento dell'utente
    @GET("paymentMethods/get/{userId}")
    @RequiresAuth
    suspend fun getPaymentMethods(@Path("userId") userId: UUID) : Response<List<PaymentMethod>?>

    @GET("paymentMethods/get/{userId}/{paymentMethodId}")
    @RequiresAuth
    suspend fun getPaymentMethod(
        @Path("userId") userId: UUID,
        @Path("paymentMethodId") paymentMethodId: Long
    ) : Response<PaymentMethod?>

    @DELETE("paymentMethods/delete/{paymentMethodId}/{userId}")
    @RequiresAuth
    suspend fun deletePaymentMethod(
        @Path("userId") userId: UUID,
        @Path("paymentMethodId") paymentMethodId: Long
    )

    // Aggiunge un nuovo metodo di pagamento
    @POST("paymentMethods/add")
    @RequiresAuth
    suspend fun addPaymentMethod(
        @Body paymentMethod: SavePaymentMethod
    ) : Response<PaymentMethod>

    @POST("orders/add")
    @RequiresAuth
    suspend fun addOrder(
        @Body checkoutRequest: CheckoutRequest
    ) : Response<SaveOrder>

}