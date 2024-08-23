package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
import com.example.ecommercefront_end.model.WishlistUpdate
import com.example.ecommercefront_end.network.WishlistApiService
import com.example.ecommercefront_end.network.WishlistItemApiService
import retrofit2.Response
import java.util.UUID

class WishlistRepository (private val wApiService : WishlistApiService, private val WIApiService: WishlistItemApiService) {

    suspend fun getWishlists(userId : UUID) = wApiService.getWishlistById(userId)

    suspend fun updateWishlist(w: Wishlist) {
        val newWishlist = WishlistUpdate(name = w.name, privacySettings = w.privacySetting)
        wApiService.updateWishlist(newWishlist)

    }

    suspend fun removeWishlist(id: Long): Response<Unit>{
        return wApiService.deleteWishlist(id)
    }

    suspend fun getAllWishlists() : List<Wishlist> {
        return try {
            val wishlists = wApiService.getAllWishlist()

            if (wishlists.isEmpty()) {
                println("Nessuna lista dei desideri trovata")
            } else {
                println("Liste dei desideri prese")
            }
            wishlists

        } catch (e: Exception) {
            println("Errore durante il recupero di tutte le liste dei desideri: ${e.message}")
            emptyList() // Restituisci una lista vuota in caso di errore
        }
    }

    suspend fun getWishlistItems(wishlistId: Long): List<WishlistItem>{
        return try {
            val wishlistItems = WIApiService.getWishlistItems(wishlistId)

            if (wishlistItems.isEmpty()) {
                println("Nessun elemento della lista dei desideri trovato")
            } else {
                println("Elementi della lista dei desideri presi")
            }
            wishlistItems

        } catch (e: Exception) {
            println("Errore durante il recupero degli elementi della lista dei desideri: ${e.message}")
            emptyList() // Restituisci una lista vuota in caso di errore
        }
    }

    suspend fun addWishlist(wishlist: Wishlist) {
        wApiService.addWishlist(wishlist)
    }

    suspend fun removeWishlistItem(id: Long): Response<Unit> {
        return WIApiService.deleteWishlistItem(id)
    }

}