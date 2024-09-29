package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.UserId
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface AddressApiService {

    @GET("addresses/{userid}")
    @RequiresAuth
    suspend fun getAddresses(@Path("userid") userid: UUID) : List<Address>?

    @GET("addresses/{userId}/{addressId}")
    @RequiresAuth
    suspend fun getAddress(@Path("userId") userId: UUID, @Path("addressId") addressId: Long) : Address?

    @GET("addresses/{userId}/default")
    @RequiresAuth
    suspend fun getDefaultAddress(@Path("userId") userId: UUID) : Address?

    @POST("addresses/{userId}/insert-address")
    @RequiresAuth
    suspend fun insertAddress(@Path("userId") userId: UUID, @Body address: SaveAddress) : Address?

    //@DELETE("addresses/{id}/delete")
    @HTTP(method = "DELETE", path = "addresses/{id}/delete", hasBody = true)
    @RequiresAuth
    suspend fun removeAddress(@Path("id") id: Long, @Body userId: UserId)

    @PUT("addresses/{id}/update-default")
    @RequiresAuth
    suspend fun updateDefaultAddress(@Path("id") id:Long, @Body userId: UserId)

    @PUT("addresses/{userId}/{addressId}/edit-address")
    @RequiresAuth
    suspend fun editAddress(@Path("userId") userId: UUID,@Path("addressId") addressId:Long, @Body address: SaveAddress)

}