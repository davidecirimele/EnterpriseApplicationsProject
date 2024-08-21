package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.WishlistItem
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface WishlistItemApiService {

    @POST("/api/v1/wishlist-items/wishlistItem/add")
    suspend fun insertWishlistItem(w: WishlistItem)

    @GET("/api/v1/wishlist-items/getByWishlistId/{idWi}")
    suspend fun getWishlistItems(@Path("idWi") idW: Long) : List<WishlistItem>

    @GET("/api/v1/wishlist-items/getAll")
    suspend fun getAllWishlistItem()

    @GET("/api/v1/wishlist-items/getByWishlist/{idWishlist}")
    suspend fun getWishlistItemByWishlist(@Path("idUser") idUser: Long)


    @DELETE("/api/v1/wishlist-items/delete/{idWi}")
    suspend fun deleteWishlistItem(@Path("idWi") idWi: Long)
}