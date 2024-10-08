package com.example.ecommercefront_end.network

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
    suspend fun addWishlist(@Body w: Wishlist)

    @GET("wishlists/get/{idW}")
    suspend fun getWishlistById(@Path("idW") idW: UUID)

    @GET("wishlists/getByUser/{idUser}")
    suspend fun getWishlistsByUser(@Path("idUser") idUser: Long)

    @GET("wishlists/getAll")
    suspend fun getAllWishlist() : List<Wishlist>

    @PUT("wishlists/update")
    suspend fun updateWishlist(@Body w: WishlistUpdate)

    @DELETE("/api/v1/wishlists/delete/{idWishlist}")
    suspend fun deleteWishlist(@Path("idWishlist") idW: Long) : Response<Unit>

}