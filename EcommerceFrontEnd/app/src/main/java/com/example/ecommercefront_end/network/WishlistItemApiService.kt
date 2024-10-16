package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.WishlistItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface WishlistItemApiService {

    @POST("wishlist-items/add")
    suspend fun insertWishlistItem(w: WishlistItem)

    @GET("wishlist-items/getByWishlistId/{idWi}")
    @RequiresAuth
    suspend fun getWishlistItems(@Path("idWi") idW: Long) : List<WishlistItem>

    @GET("wishlist-items/getAll")
    @RequiresAuth
    suspend fun getAllWishlistItem()

    @GET("wishlist-items/getByWishlist/{idWishlist}")
    @RequiresAuth
    suspend fun getWishlistItemByWishlist(@Path("idUser") idUser: Long)


    @DELETE("wishlist-items/delete/{idWishlistItem}")
    @RequiresAuth
    suspend fun deleteWishlistItem(@Path("idWishlistItem") idWi: Long): Response<Unit>
}