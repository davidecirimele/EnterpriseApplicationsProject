package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.Wishlist
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID


interface WishlistApiService {

    @POST("wishlists/add")
    @RequiresAuth
    suspend fun addWishlist(@Body w: Wishlist)

    @GET("wishlists/get/{idW}")
    @RequiresAuth
    suspend fun getWishlistById(@Path("idW") idW: UUID)

    @GET("wishlists/getByUser/{idUser}")
    @RequiresAuth
    suspend fun getWishlistsByUser(@Path("idUser") idUser: UUID) : List<Wishlist>

    @GET("wishlists/getAll")
    @RequiresAuth
    suspend fun getAllWishlist() : List<Wishlist>

    @PUT("wishlists/update")
    @RequiresAuth
    suspend fun updateWishlist(@Body w: Wishlist): Response<Unit>

    @DELETE("wishlists/delete/{idWishlist}")
    @RequiresAuth
    suspend fun deleteWishlist(@Path("idWishlist") idW: Long) : Response<Unit>

    @GET("wishlists/share")
    @RequiresAuth
    suspend fun shareWishlist(@Body wishlist: Wishlist) : Map<String, String>

    @GET("wishlists/getOfFriend/{idUser}")
    @RequiresAuth
    suspend fun getFriendWishlists(@Path("idUser") idUser: UUID): List<Wishlist>

    @POST("wishlists/join/{idUser}/{token}")
    @RequiresAuth
    suspend fun joinWishlist(@Path("idUser") idUser: UUID, @Path("token") token: String): Response<Boolean>

    @POST("wishlists/unshare/{idUser}")
    @RequiresAuth
    suspend fun unshareWishlist(@Path("idUser") idUser: UUID, @Body wishlist: Wishlist): Response<Boolean>


}