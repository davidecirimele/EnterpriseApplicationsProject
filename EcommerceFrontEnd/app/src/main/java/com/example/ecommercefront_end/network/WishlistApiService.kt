package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistUpdate
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID


interface WishlistApiService {

    @POST("/wishlist/add")
    suspend fun insertWishlist(w: Wishlist)

    @GET("/wishlist/get/{idW}")
    suspend fun getWishlists(@Path("idW") idW: UUID)

    @GET("/wishlist/getByUser/{idUser}")
    suspend fun getWishlistByUser(@Path("idUser") idUser: Long)

    @GET("/wishlist/getAll")
    suspend fun getAllWishlist()

    @PUT("/wishlist/edit")
    suspend fun updateWishlist(@Body w: WishlistUpdate)

    @DELETE("/wishlist/delete/{idW}")
    suspend fun deleteWishlist(@Path("idW") idW: Long)

}