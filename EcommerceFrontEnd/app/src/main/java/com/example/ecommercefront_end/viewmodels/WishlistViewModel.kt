package com.example.ecommercefront_end.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.model.Group
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
import com.example.ecommercefront_end.repository.WishlistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(private val wRepository: WishlistRepository) : ViewModel() {

    private val _wishlists = MutableStateFlow<List<Wishlist>>(emptyList())
    val wishlists: StateFlow<List<Wishlist>> = _wishlists.asStateFlow()

    private val _wishlistItems = MutableStateFlow<List<WishlistItem>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItem>> = _wishlistItems.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init{
        loadWishlistsFromBD()
    }

    private fun loadWishlistsFromBD(){
        viewModelScope.launch {
            try{
                _wishlists.value = wRepository.getAllWishlists()
                if(_wishlists.value.isEmpty()){
                    _error.value = "Nessuna wishlist trovata"
                }
            } catch (e: Exception){
                _error.value = "Errore durante il caricamento delle wishlist: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _wishlistFlow = MutableStateFlow<List<WishlistItem>?>(null)
    val wishlistFlow: Flow<List<WishlistItem>?> = _wishlistFlow

    fun loadWishlistItemsFromDB(wishlistId: Long){
        viewModelScope.launch {
            try{
                _wishlistItems.value = wRepository.getWishlistItems(wishlistId)
                if(_wishlistItems.value.isEmpty()){
                    _error.value = "WishlistItems vuota"
                }
            } catch (e: Exception){
                _error.value = "Errore durante il caricamento degli elementi della wishlist: ${e.message}"
            }
        }
    }

    fun loadWishlists(id : Long){
        viewModelScope.launch {
            val wishlist = _wishlists.value.find { it.id == id }

        }
    }


    fun addWishlist(name: String, isPrivate: Boolean, otherParam: String) {
        viewModelScope.launch {
            try {
                // Crea una lista vuota di elementi per la nuova wishlist
                val wItemsList = mutableListOf<WishlistItem>()

                // Crea un gruppo predefinito con un nome e ID fittizio, dato che non hai informazioni su gruppi esistenti
                val defaultGroup = Group(
                    id = 5L, // ID fittizio, da aggiornare se necessario
                    groupName = "Famiglia_Test", // Nome del gruppo fittizio
                    members = emptyList() // Nessun membro per ora
                )

                // Imposta la privacy della wishlist
                val privacySetting = if (isPrivate) "Private" else "Public"

                // Crea un nuovo oggetto Wishlist con i parametri forniti
                val newWishlist = Wishlist(
                    id = 50, // ID sarà generato dal database
                    name = name,
                    items = wItemsList, // Lista vuota di elementi
                    user = null, // Nessun utente associato dato che non è loggato
                    group = defaultGroup, // Gruppo predefinito
                    privacySetting = privacySetting
                )

                // Salva la nuova wishlist nel database tramite il repository
                wRepository.addWishlist(newWishlist)

                // Potresti aggiornare la lista delle wishlist nel ViewModel qui, se necessario
            } catch (e: Exception) {
                // Gestisci l'errore
                // Puoi aggiungere un log o mostrare un messaggio di errore all'utente
                Log.e("addWishlist", "Errore durante la creazione della wishlist: ${e.message}")
            }
        }
    }

    fun removeWishlistItem(id: Long) {
       viewModelScope.launch {
           try {
               val response = wRepository.removeWishlistItem(id)
                if (response.isSuccessful) {
                    Log.d("rimosso WI con id", id.toString())
                    _wishlistItems.value = _wishlistItems.value.filter{ it.id != id }
                     Log.d("removeWishlistItem", "Elemento della wishlist rimosso con successo")
                } else {
                    Log.e("removeWishlistItem", "Non rimosso WI con id: $id")
                     Log.e("removeWishlistItem", "Errore durante la rimozione dell'elemento della wishlist: ${response.errorBody()}")
                }
           } catch (e: Exception) {
               Log.e("removeWishlistItem", "Errore durante la rimozione dell'elemento della wishlist: ${e.message}")
           }
       }

    }

    fun removeWishlist(wishlistId: Long){
        viewModelScope.launch {
            try {
                // Elimina la wishlist
                val wResponse = wRepository.removeWishlist(wishlistId)
                if (wResponse.isSuccessful) {
                    _wishlists.value = _wishlists.value.filter { it.id != wishlistId }
                    Log.d("removeWishlist", "Wishlist rimossa con successo")
                } else {
                    Log.e("removeWishlist", "Errore durante la rimozione della wishlist: ${wResponse.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("removeWishlist", "Errore durante la rimozione della wishlist: ${e.message}")
            }
        }
    }


}