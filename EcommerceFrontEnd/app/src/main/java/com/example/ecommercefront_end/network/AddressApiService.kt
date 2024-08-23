package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.UserId
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface AddressApiService {

    @GET("/addresses/{userid}")
    suspend fun getAddresses(@Path("userid") userid: UUID) : List<Address>?

    @GET("/addresses/{userid}/{id}")
    suspend fun getAddress(@Path("userId") userId: UUID, @Path("addressId") addressId: Long) : Address?

    @POST("/addresses/{userid}/insert-address")
    suspend fun insertAddress(@Path("userId") userId: UUID, @Body address: SaveAddress) : Address?

    @DELETE("/addresses/{id}/delete")
    suspend fun deleteAddress(@Path("id") id: Long, @Body userId: UserId)

    @PUT("/addresses/{id}/update-default")
    suspend fun updateDefaultAddress(@Path("id") id:Long, @Body userId: UserId)

    @PUT("/addresses/{userId}/{addressId}/edit-address")
    suspend fun editAddress(@Path("userId") userId: UUID,@Path("addressId") addressId:Long, @Body address: SaveAddress)

}