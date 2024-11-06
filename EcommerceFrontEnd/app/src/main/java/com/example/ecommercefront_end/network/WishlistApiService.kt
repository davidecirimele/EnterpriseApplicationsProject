package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.SaveWishlist
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistPrivacy
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID


interface WishlistApiService {

    @POST("wishlists/add/{idUser}/{wName}/{wPrivacySetting}")
    @RequiresAuth
    suspend fun addWishlist(@Path("idUser") idUser: UUID, @Path("wName") wName: String, @Path("wPrivacySetting") wPrivacySetting: WishlistPrivacy): Response<Boolean>

    @GET("wishlists/get/{idWishlist}/{idUser}")
    @RequiresAuth
    suspend fun getById(@Path("idW") idW: UUID)

    @GET("wishlists/getByUser/{idUser}")
    @RequiresAuth
    suspend fun getWishlistsByUser(@Path("idUser") idUser: UUID) : List<Wishlist>

    @GET("wishlists/getAll")
    @RequiresAuth
    suspend fun getAllWishlist() : List<Wishlist>

    @PUT("wishlists/update/{idUser}")
    @RequiresAuth
    suspend fun updateWishlist(@Body w: Wishlist, @Path("idUser") idUser: UUID): Response<Unit>

    @DELETE("wishlists/delete/{idWishlist}/{idUser}")
    @RequiresAuth
    suspend fun deleteWishlist(@Path("idWishlist") idW: Long, @Path("idUser") idUser: UUID) : Response<Unit>

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