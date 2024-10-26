package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.SaveWishlist
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
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

    suspend fun getFriendWishlist(userId : UUID): List<Wishlist>{
        return try {
            val wishlists = wApiService.getFriendWishlists(userId)
            println("Cercando wishlist per amico $userId")
            if (wishlists.isEmpty()) {
                println("Nessuna lista dei desideri trovata per l amico")
            } else {
                println("Liste dei desideri amico prese")
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

    suspend fun getWishlistItems(wishlistId: Long, userId: UUID): List<WishlistItem>{
        return try {
            val wishlistItems = WIApiService.getByWishlist(wishlistId, userId)

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

    suspend fun addWishlist(wishlist: SaveWishlist) {
        wApiService.addWishlist(wishlist)
    }
    suspend fun updatePrivacySettings(wishlist: Wishlist) {
        wApiService.updateWishlist(wishlist)
    }

    suspend fun removeWishlistItem(id: Long, idUser : UUID): Response<Unit> {
        return WIApiService.removeItem(id, idUser)
    }
    suspend fun shareWishlist(wishlist: Wishlist): Map<String, String> {
        return wApiService.shareWishlist(wishlist)
    }

    suspend fun joinWishlist(userId: UUID , token: String ): Response<Boolean> {
        return  wApiService.joinWishlist(userId, token)
    }

    suspend fun getWishlistsByFriends(id: UUID): List<Wishlist> {
        return wApiService.getFriendWishlists(id)
    }

    suspend fun unshareWishlist(id: UUID, wishlist: Wishlist): Response<Boolean> {
        return wApiService.unshareWishlist(id, wishlist)
    }

}