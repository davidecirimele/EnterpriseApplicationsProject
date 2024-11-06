package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.UserId
import retrofit2.Response
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
    suspend fun getAddresses(@Path("userid") userid: UUID) : Response<List<Address>>

    @GET("addresses/{userId}/{addressId}")
    @RequiresAuth
    suspend fun getAddress(@Path("userId") userId: UUID, @Path("addressId") addressId: Long) : Response<Address?>

    @GET("addresses/{userId}/default")
    @RequiresAuth
    suspend fun getDefaultAddress(@Path("userId") userId: UUID) : Response<Address?>

    @POST("addresses/{userId}/insert-address")
    @RequiresAuth
    suspend fun insertAddress(@Path("userId") userId: UUID, @Body address: SaveAddress) : Response<Address?>

    @DELETE("addresses/{userId}/{id}/delete")
    @RequiresAuth
    suspend fun removeAddress(@Path("id") id: Long, @Path ("userId") userId: UUID)

    @PUT("addresses/{userId}/{id}/update-default")
    @RequiresAuth
    suspend fun updateDefaultAddress(@Path("id") id:Long, @Path ("userId") userId: UUID) : Response<Address>

    @PUT("addresses/{userId}/{addressId}/edit-address")
    @RequiresAuth
    suspend fun editAddress(@Path("userId") userId: UUID,@Path("addressId") addressId:Long, @Body address: SaveAddress): Response<Address>

}