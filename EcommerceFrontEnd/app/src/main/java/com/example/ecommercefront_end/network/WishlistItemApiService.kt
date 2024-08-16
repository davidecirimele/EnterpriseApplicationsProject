package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.WishlistItem
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface WishlistItemApiService {

    @POST("/wishlistItem/add")
    suspend fun insertWishlistItem(w: WishlistItem)

    @GET("/wishlistItem/get/{idWi}")
    suspend fun getWishlistItem(@Path("idWi") idW: Long)

    @GET("/wishlistItem/getAll")
    suspend fun getAllWishlistItem()

    @GET("/wishlistItem/getByWishlist/{idWishlist}")
    suspend fun getWishlistItemByWishlist(@Path("idUser") idUser: Long)


    @DELETE("/wishlistItem/delete/{idWi}")
    suspend fun deleteWishlistItem(@Path("idWi") idWi: Long)
}