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

    @POST("/api/v1/wishlists/add")
    suspend fun addWishlist(w: Wishlist)

    @GET("/api/v1/wishlists/get/{idW}")
    suspend fun getWishlistById(@Path("idW") idW: UUID)

    @GET("/api/v1/wishlists/getByUser/{idUser}")
    suspend fun getWishlistsByUser(@Path("idUser") idUser: Long)

    @GET("/api/v1/wishlists/getAll")
    suspend fun getAllWishlist() : List<Wishlist>

    @PUT("/api/v1/wishlists/update")
    suspend fun updateWishlist(@Body w: WishlistUpdate)

    @DELETE("/api/v1/wishlists/delete/{idW}")
    suspend fun deleteWishlist(@Path("idW") idW: Long)

}