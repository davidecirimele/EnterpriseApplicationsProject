package com.example.ecommercefront_end.viewmodels

import android.util.Log
import androidx.compose.runtime.currentCompositionErrors
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Group
import com.example.ecommercefront_end.model.SaveWishlist
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
import com.example.ecommercefront_end.model.WishlistPrivacy
import com.example.ecommercefront_end.repository.GroupRepository
import com.example.ecommercefront_end.repository.WishlistRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.UUID

class WishlistViewModel(private val wRepository: WishlistRepository, private val groupRepository: GroupRepository) : ViewModel() {

    private val _wishlists = MutableStateFlow<List<Wishlist>>(emptyList())
    val wishlists: StateFlow<List<Wishlist>> = _wishlists.asStateFlow()

    private val _onlyMyWishlists = MutableStateFlow<List<Wishlist>>(emptyList())
    val onlyMyWishlists: StateFlow<List<Wishlist>> = _onlyMyWishlists.asStateFlow()

    private val _friendWishlists = MutableStateFlow<List<Wishlist>>(emptyList())
    val friendWishlists: StateFlow<List<Wishlist>> = _friendWishlists.asStateFlow()


    private val _wishlistItems = MutableStateFlow<List<WishlistItem>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItem>> = _wishlistItems.asStateFlow()

    private val _userSelectedByAdmin = MutableStateFlow<UUID?>(null)
    val userSelectedByAdmin: StateFlow<UUID?> = _userSelectedByAdmin.asStateFlow()

    private val _tokenToShare = MutableStateFlow<String>("")
    val tokenToShare: StateFlow<String> = _tokenToShare.asStateFlow()

    private val _isLoadingWishlist = MutableStateFlow(true)
    val isLoadingWishlist : StateFlow<Boolean> = _isLoadingWishlist.asStateFlow()

    private val _isLoadingItems = MutableStateFlow(true)
    val isLoadingItems : StateFlow<Boolean> = _isLoadingItems.asStateFlow()

    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar: StateFlow<Boolean> get() = _showSnackbar

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> get() = _snackbarMessage


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()



    suspend fun fetchWishlists(idUser: UUID? = null) {
        viewModelScope.launch {
            _isLoadingWishlist.value = true
            try {
                val userId = idUser ?: SessionManager.user?.id
                userId?.let {
                    val friendWishlists = wRepository.getFriendWishlist(it)
                    val myWishlists = wRepository.getWishlistsByUser(it)
                    _wishlists.value = myWishlists + friendWishlists

                    if (SessionManager.user?.role == "ROLE_ADMIN") {
                        _userSelectedByAdmin.value = it
                    }
                } ?: Log.d("UserDebug", "User is null")

                _isLoadingWishlist.value = false
            } catch (e: Exception) {
                _error.value = "Errorduring wishlist fetching: ${e.message}"
            }
        }
    }


    fun fetchWishlistItems(wishlistId: Long, userId: UUID) {
        viewModelScope.launch {
            _isLoadingItems.value = true
            val currentUser = SessionManager.user
            val isAdmin = currentUser?.role == "ROLE_ADMIN"
            try {
                if (currentUser != null){
                    if (isAdmin) {
                        _wishlistItems.value = wRepository.getWishlistItems(wishlistId, userId)
                    }
                    else {
                        _wishlistItems.value = wRepository.getWishlistItems(wishlistId, currentUser.id)
                    }
                }
                else{
                    Log.d("UserDebug", "User is null")
                }
                _isLoadingItems.value = false
            } catch (e: Exception) {
                Log.e(
                    "fetchWishlistItems",
                    "Error during wishlist items fetching: ${e.message}"
                )
            }
        }
    }


    fun joinWishlist(token: String) {
        viewModelScope.launch {
            try {
                val userId = if (SessionManager.user?.role == "ROLE_ADMIN") {
                    _userSelectedByAdmin.value
                } else {SessionManager.user?.id
                }

                userId?.let {
                    val response = groupRepository.addUser(it, token)
                    if (response.isSuccessful) {
                        val message = when (response.body()) {
                            1 -> "You have successfully joined the wishlist"
                            0 -> "You joined but at the moment the wishlist is private"
                            else -> "Error during joining the wishlist: + ${response.errorBody()}"
                        }
                        triggerSnackbar(message)
                    } else {
                        triggerSnackbar("Error during joining the wishlist: + ${response.errorBody()}")
                    }
                } ?: triggerSnackbar("Error: User not found")

                if (SessionManager.user?.role == "ROLE_ADMIN") {
                    fetchWishlists(userSelectedByAdmin.value)
                } else
                    fetchWishlists(null) // DA SISTEMARE

            } catch (e: Exception) {
                Log.e("joinWishlist", "Error during joining the wishlist: ${e.message}")
                triggerSnackbar("Error during joining the wishlist ${e.message}")
            }
        }
    }

    fun unshareWishlist(wishlist: Wishlist) {
        var message = ""
        viewModelScope.launch {
            try {
                val currentUser = SessionManager.user
                var response : Response<Boolean>? = null
                val isAdmin = currentUser?.role == "ROLE_ADMIN"

                if (currentUser != null){
                    if (isAdmin) {
                        response  = userSelectedByAdmin?.let { it.value?.let { it1 ->
                            wishlist.group?.let { it2 ->
                                groupRepository.removeUser(
                                    it2.id, it1, it1
                                )
                            }
                        } }
                    }
                    else {
                        response = currentUser?.let {
                            wishlist.group?.let { it1 ->
                                groupRepository.removeUser(
                                    it1.id, it.id, currentUser.id
                                )
                            }
                        }
                    }
                }

                if (response != null && response.isSuccessful) {
                    Log.d("unshareWishlist", "Wishlist non più condivisa con successo")
                    message = "Wishlist no longer shared successfully"
                    fetchWishlists(null)

                } else {
                    if (response != null) {
                        Log.e(
                            "unshareWishlist",
                            "Error during wishlist exiting: ${response.errorBody()}"
                        )
                        message = "Error during wishlist exiting: ${response.errorBody()}"
                    }
                }
                triggerSnackbar(message)
            } catch (e: Exception) {
                Log.e(
                    "unshareWishlist",
                    "Error during wishlist exiting: ${e.message}"
                )
                message = "Errore during wishlist exiting: ${e.message}"
                triggerSnackbar(message)
            }
        }
    }

    fun addWishlist(wishlistName: String, privacySetting: WishlistPrivacy)  {
        var message = ""
        viewModelScope.launch { // Avvia un nuovo processo in background
            try {
                // Crea una lista vuota di elementi per la nuova wishlist

                val wItemsList = mutableListOf<WishlistItem>()
                val group = Group(0, "default", emptyList())

                // Crea un gruppo predefinito con un nome e ID fittizio, dato che non hai informazioni su gruppi esistenti
                // Imposta la privacy della wishlist

                // Crea un nuovo oggetto Wishlist con i parametri forniti
                val newWishlist = SaveWishlist(
                    name = wishlistName,
                    items = wItemsList,
                    user = SessionManager.user, // Nessun utente associato dato che non è loggato
                    group = null,
                    privacySetting = privacySetting,
                    wishlistToken = ""
                )

                // Salva la nuova wishlist nel database tramite il repository
                Log.e("addWishlist", newWishlist.toString())

                val currentUser = SessionManager.user
                var response : Response<Boolean> ? = null
                val isAdmin = currentUser?.role == "ROLE_ADMIN"

                if (currentUser != null){

                    if (isAdmin) {
                        response  = userSelectedByAdmin.let { it.value?.let { it1 ->
                            wRepository.addWishlist(newWishlist, it1)
                            }
                        }
                    }
                    else {
                        response  = currentUser.let { wRepository.addWishlist(newWishlist, it.id) }

                    }
                }
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.d("addWishlist", "Wishlist creata con successo")

                        if (isAdmin) {
                            fetchWishlists(userSelectedByAdmin.value)
                            message = "Wishlist "  + newWishlist.name +  " created successfully"
                        }
                        else {
                            fetchWishlists()
                            message = "Wishlist "  + newWishlist.name +  " created successfully"
                        }
                    }
                    else {
                        Log.e(
                            "addWishlist",
                            "Errore durante la creazione della wishlist: ${response.errorBody()}"
                        )
                        message = "Error during wishlist creation"
                    }
                }
                triggerSnackbar(message)

                // Potresti aggiornare la lista delle wishlist nel ViewModel qui, se necessario
            } catch (e: Exception) {
                // Gestisci l'errore
                // Puoi aggiungere un log o mostrare un messaggio di errore all'utente
                Log.e("addWishlist", "Error during wishlist creation: ${e.message}")
                message = "Error during wishlist creation"
                triggerSnackbar(message)
            }
        }

    }

    fun updateWishlist(
        id: Long,
        name: String?,
        privacySettings: WishlistPrivacy?,
    ) {
        var message = ""
        viewModelScope.launch {
            try {
                val currentUser = SessionManager.user
                val wishlist = _wishlists.value.find { it.id == id }
                if (wishlist != null) {
                    val updatedWishlist = wishlist.copy()

                    if (name!= null && name != "") {
                        updatedWishlist.name = name
                        Log.d(
                            "updateWishlistPrivacy",
                            "Nuovo Nome della wishlist ${updatedWishlist.name} aggiornato con successo"
                        )
                    }
                    if (privacySettings != null) {
                        updatedWishlist.privacySetting = privacySettings
                        Log.d(
                            "updateWishlistPrivacy",
                            "Nuove imp privacy ${updatedWishlist.privacySetting} aggiornato con successo"
                        )
                    }

                    var response : Response<Unit> ? = null
                    if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                       response = SessionManager.user?.let { wRepository.updateWishlist(updatedWishlist, it.id) }

                    }
                    else if (currentUser != null && currentUser.role == "ROLE_ADMIN") {
                        response = userSelectedByAdmin.let { it.value?.let { it1 ->
                            wRepository.updateWishlist(updatedWishlist, it1)
                        }
                        }
                    }

                    if (response != null && response.isSuccessful) {
                        _wishlists.value =
                            _wishlists.value.map { if (it.id == id) updatedWishlist else it }

                        Log.d("updateWishlistPrivacy", "Wishlist updated successfully")
                        triggerSnackbar("Wishlist updated successfully")

                    } else {
                        if (response != null) {
                            Log.e("updateWishlistPrivacy", "Wishlist error during updating: ${response.errorBody()}")
                        }
                        triggerSnackbar("Error during wishlist updating")

                    }

                } else {
                    Log.e("updateWishlistPrivacy", "Wishlist non trovata con id: $id")
                    triggerSnackbar("Error during wishlist updating")

                }
            } catch (e: Exception) {
                Log.e(
                    "updateWishlistPrivacy",
                    "Error during wishlist updating: ${e.message}"
                )
                triggerSnackbar("Error during wishlist updating: ${e.message}")
            }
        }
    }

    fun deleteWishlist(wishlistId: Long, idUser: UUID) {
        var message = ""
        viewModelScope.launch {
            try {
                // Elimina la wishlist
                val wResponse = wRepository.removeWishlist(wishlistId, idUser)
                if (wResponse.isSuccessful) {
                    _wishlists.value = _wishlists.value.filter { it.id != wishlistId }
                    Log.d("removeWishlist", "Wishlist rimossa con successo")
                    message = "Wishlist deleted successfully"
                } else {
                    Log.e(
                        "removeWishlist",
                        "Error during the removal of the wishlist: ${wResponse.errorBody()}"
                    )
                    message = "Error during the removal of the wishlist"
                }
                triggerSnackbar(message)
            }
            catch (e: Exception) {
                Log.e(
                    "removeWishlist",
                    "Error during the removal of the wishlist: ${e.message}"
                )
                message = "Error during the removal of the wishlist ${e.message}"
                triggerSnackbar(message)

            }
        }

    }


    fun addWishlistItem(BookId: Long, wishlistId: Long) {
        viewModelScope.launch {
            try {
                val userId = if (SessionManager.user?.role == "ROLE_ADMIN") {
                    userSelectedByAdmin.value
                } else {
                    SessionManager.user?.id
                }

                userId?.let {
                    val response = wRepository.addWishlistItem(BookId, wishlistId, it)
                    if (response.isSuccessful) {
                        triggerSnackbar("Book added successfully to the wishlist")
                    } else {
                        triggerSnackbar("Error during the addition of the wishlist item ${response.errorBody()}")
                    }
                } ?: triggerSnackbar("Error: User not found")
            } catch (e: Exception) {
                triggerSnackbar("Error during the addition of the wishlistitem ${e.message}")
            }
        }
    }


    fun deleteWishlistItem(id: Long) {
        viewModelScope.launch {
            try {
                val userId = if (SessionManager.user?.role == "ROLE_ADMIN") {
                    userSelectedByAdmin.value
                } else{
                    SessionManager.user?.id
                }

                userId?.let {
                    val response = wRepository.removeWishlistItem(id, it)
                    if (response.isSuccessful) {
                        _wishlistItems.value = _wishlistItems.value.filter { item -> item.id != id }
                        triggerSnackbar("Book removed successfully from the wishlist")
                    } else {
                        triggerSnackbar("Error during the removal of the wishlist item ${response.errorBody()}")
                    }
                } ?: triggerSnackbar("Error: User not found")
            } catch (e: Exception){
                triggerSnackbar("Error during the removal of the wishlist item: ${e.message}")
            }
        }
    }


    fun setShowSnackbar(b: Boolean) {
        _showSnackbar.value = b

    }

    fun triggerSnackbar(message: String) {
        _snackbarMessage.value = message
        _showSnackbar.value = true
    }

    fun onLogout(){
        //viewModelScope.cancel()
        _wishlists.value = emptyList()
        _onlyMyWishlists.value = emptyList()
        _wishlistItems.value = emptyList()
        _friendWishlists.value = emptyList()
        _userSelectedByAdmin.value = null
        _tokenToShare.value = ""
    }

}

