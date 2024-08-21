package com.example.ecommercefront_end.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
import com.example.ecommercefront_end.repository.WishlistRepository
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




}