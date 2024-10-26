package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.WishlistItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID


interface WishlistItemApiService {

    @POST("wishlist-items/add")
    @RequiresAuth
    suspend fun addItem(w: WishlistItem)

    @GET("wishlist-items/getByIdWishlist/{idWishlist}/{idUser}")
    @RequiresAuth
    suspend fun getByWishlist(@Path("idWishlist") idW: Long, @Path ("idUser") idUser: UUID) : List<WishlistItem>

    @GET("wishlist-items/getAll")
    @RequiresAuth
    suspend fun getAllWishlistItem()


    @DELETE("wishlist-items/delete/{idItem}/{idUser}")
    @RequiresAuth
    suspend fun removeItem(@Path("idItem") idItem: Long, @Path("idUser") idUser: UUID): Response<Unit>
}
