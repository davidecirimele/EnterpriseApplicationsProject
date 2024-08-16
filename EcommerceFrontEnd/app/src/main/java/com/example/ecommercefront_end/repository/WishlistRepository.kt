package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistUpdate
import com.example.ecommercefront_end.network.WishlistApiService
import java.util.UUID

class WishlistRepository (

    private val apiService : WishlistApiService,
)
{
    suspend fun getWishlists(userId : UUID) = apiService.getWishlists(userId)

    suspend fun updateWishlist(w: Wishlist) {
        val newWishlist = WishlistUpdate(name = w.name, privacySettings = w.privacySetting)
        apiService.updateWishlist(newWishlist)

    }

    suspend fun removeWishlist(w: Wishlist){
        apiService.deleteWishlist(w.id)
    }
}