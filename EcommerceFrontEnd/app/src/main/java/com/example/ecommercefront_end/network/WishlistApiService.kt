package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistUpdate
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

    @POST("wishlists/share")
    suspend fun shareWishlist(wishlist: Wishlist) : Response<String>

    @POST("wishlists/join/{idWishlist}")
    suspend fun joinWishlist(token: Any, wishlist: Wishlist): Any

}