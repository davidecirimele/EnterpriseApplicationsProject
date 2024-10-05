package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
import com.example.ecommercefront_end.model.WishlistUpdate
import com.example.ecommercefront_end.network.WishlistApiService
import com.example.ecommercefront_end.network.WishlistItemApiService
import retrofit2.Response
import java.util.UUID

class WishlistRepository (private val wApiService : WishlistApiService, private val WIApiService: WishlistItemApiService) {



    suspend fun updateWishlist(w: Wishlist) {
        wApiService.updateWishlist(w)

    }

    suspend fun removeWishlist(id: Long): Response<Unit>{
        return wApiService.deleteWishlist(id)
    }

    suspend fun getWishlistsByUser(userId : UUID): List<Wishlist>{
        return try {
            val wishlists = wApiService.getWishlistsByUser(userId)
            println("Cercando wishlist per utente "+ userId)
            if (wishlists.isEmpty()) {
                println("Nessuna lista dei desideri trovata per l utente")
            } else {
                println("Liste dei desideri utente prese")
            }
            wishlists

        } catch (e: Exception) {
            println("Errore durante il recupero di tutte le liste dei desideri: ${e.message}")
            emptyList() // Restituisci una lista vuota in caso di errore
        }
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
    suspend fun updatePrivacySettings(wishlist: Wishlist) {
        wApiService.updateWishlist(wishlist)
    }

    suspend fun removeWishlistItem(id: Long): Response<Unit> {
        return WIApiService.deleteWishlistItem(id)
    }
    suspend fun shareWishlist(wishlist: Wishlist): Response<String> {
        return wApiService.shareWishlist(wishlist)
    }

    suspend fun joinWishlist(token: Any, wishlist: Wishlist): Any {
        return  wApiService.joinWishlist(token, wishlist)
    }

}