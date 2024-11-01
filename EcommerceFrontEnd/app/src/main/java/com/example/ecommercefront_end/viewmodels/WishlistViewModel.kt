package com.example.ecommercefront_end.viewmodels

import android.util.Log
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
            _isLoadingWishlist.value = true // Imposta il caricamento a vero all'inizio del processo
            val currentUser = SessionManager.user

            try {
                if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                    _friendWishlists.value = wRepository.getFriendWishlist(currentUser.id)
                    Log.d("Wishlist", "friendwishlist ${_friendWishlists.value}")
                    _onlyMyWishlists.value = wRepository.getWishlistsByUser(currentUser.id)
                    Log.d("Wishlist", "mywishlist ${_onlyMyWishlists.value}")

                    _wishlists.value = _onlyMyWishlists.value + _friendWishlists.value


                } else if (currentUser != null && currentUser.role == "ROLE_ADMIN" && idUser != null) {
                    _friendWishlists.value = wRepository.getFriendWishlist(idUser)
                    _onlyMyWishlists.value = wRepository.getWishlistsByUser(idUser)

                    _wishlists.value = _onlyMyWishlists.value + _friendWishlists.value
                    _userSelectedByAdmin.value = idUser
                }
                else {
                    Log.d("UserDebug", "User is null")
                }
                _isLoadingWishlist.value = false

            } catch (e: Exception) {
                _error.value = "Errore durante il caricamento delle wishlist: ${e.message}"
            }
        }
    }


    fun fetchWishlistItems(wishlistId: Long, userId: UUID) {
        viewModelScope.launch {
            _isLoadingItems.value = true
            val currentUser = SessionManager.user
            try {
                if (currentUser != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                    _wishlistItems.value = wRepository.getWishlistItems(wishlistId, currentUser.id)

                }
                else if(SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN"){
                    _wishlistItems.value = wRepository.getWishlistItems(wishlistId, userId)
                }
                else{
                    Log.d("UserDebug", "User is null")
                }
                _isLoadingItems.value = false
            } catch (e: Exception) {
                Log.e(
                    "fetchWishlistItems",
                    "Errore durante il caricamento degli elementi della wishlist: ${e.message}"
                )
            }
        }
    }



     fun joinWishlist(token: String) {
        viewModelScope.launch {
            var message = ""
            try {
                val currentUser = SessionManager.user
                var response : Response<Boolean>? = null

                if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                    Log.d("joinWishlist", "primo if, idUser = ${currentUser.id}")
                    response = currentUser?.let { groupRepository.addUser(it.id, token) }

                } else if (currentUser != null && currentUser.role == "ROLE_ADMIN") {
                    Log.d("joinWishlist", "secondo if, idUser = ${currentUser.id} ${_userSelectedByAdmin}")

                    response = _userSelectedByAdmin.value?.let { groupRepository.addUser(it, token) }

                }
                fetchWishlists(null)// DA SISTEMARE


                if (response != null && response.isSuccessful) {
                    Log.d("joinWishlist", "Utente aggiunto con successo alla wishlist")
                    message = "Ti sei unito alla wishlist con successo"
                } else {
                    //Log.e("joinWishlist", "Errore durante l'aggiunta dell'utente alla wishlist: ${response.errorBody()}")
                    message = "Errore durante l'aggiunta dell'utente alla wishlist"
                }
                triggerSnackbar(message)

            } catch (e: Exception) {
                Log.e(
                    "joinWishlist",
                    "Errore durante l'aggiunta dell'utente alla wishlist: ${e.message}"
                )
                message = "Errore durante l'aggiunta dell'utente alla wishlist"
                triggerSnackbar(message)

            }
        }
    }

    fun unshareWishlist(wishlist: Wishlist) {
        var message = ""
        viewModelScope.launch {
            try {
                val currentUser = SessionManager.user
                var response : Response<Boolean>? = null

                if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                    response = currentUser?.let {
                        wishlist.group?.let { it1 ->
                            groupRepository.removeUser(
                                it1.id, it.id
                            )
                        }
                    }
                } else if (currentUser != null && currentUser.role == "ROLE_ADMIN") {

                    response  = userSelectedByAdmin?.let { it.value?.let { it1 ->
                        wishlist.group?.let { it2 ->
                            groupRepository.removeUser(
                                it2.id, it1
                            )
                        }
                    } }
                }

                if (response != null && response.isSuccessful) {
                    Log.d("unshareWishlist", "Wishlist non più condivisa con successo")
                    message = "Wishlist non più condivisa con successo"
                } else {
                    if (response != null) {
                        Log.e(
                            "unshareWishlist",
                            "Errore durante l'uscita della condivisione della wishlist: ${response.errorBody()}"
                        )
                        message = "Errore durante l'uscita della condivisione della wishlist"
                    }
                }
                triggerSnackbar(message)
            } catch (e: Exception) {
                Log.e(
                    "unshareWishlist",
                    "Errore durante l'eliminazione della condivisione della wishlist: ${e.message}"
                )
                message = "Errore durante l'eliminazione della condivisione della wishlist"
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
                var response : Unit ? = null

                if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                    response  = currentUser?.let { wRepository.addWishlist(newWishlist, it.id) }

                } else if (currentUser != null && currentUser.role == "ROLE_ADMIN") {
                    response  = userSelectedByAdmin?.let { it.value?.let { it1 ->
                        wRepository.addWishlist(newWishlist, it1)
                    } }
                }

                if (response != null) {
                    Log.d("addWishlist", "Wishlist creata con successo")
                    fetchWishlists()
                    message = "Wishlist creata con successo"
                    triggerSnackbar(message)
                }
                // Potresti aggiornare la lista delle wishlist nel ViewModel qui, se necessario
            } catch (e: Exception) {
                // Gestisci l'errore
                // Puoi aggiungere un log o mostrare un messaggio di errore all'utente
                Log.e("addWishlist", "Errore durante la creazione della wishlist: ${e.message}")
                message = "Errore durante la creazione della wishlist"
                triggerSnackbar(message)
            }
        }

    }

    fun updateWishlist(
        id: Long,
        name: String,
        privacySettings: WishlistPrivacy,
        group: Group?
    ) {
        var message = ""
        viewModelScope.launch {
            try {
                val wishlist = _wishlists.value.find { it.id == id }
                if (wishlist != null) {
                    val updatedWishlist = wishlist.copy()

                    if (!name.equals("")) {
                        updatedWishlist.name = name
                        Log.d(
                            "updateWishlistPrivacy",
                            "Nuovo Nome della wishlist ${updatedWishlist.name} aggiornato con successo"
                        )
                    }
                    if (!privacySettings.equals("")) {
                        updatedWishlist.privacySetting = privacySettings
                        Log.d(
                            "updateWishlistPrivacy",
                            "Nuove imp privacy ${updatedWishlist.privacySetting} aggiornato con successo"
                        )
                    }
                    if (group != null) {
                        updatedWishlist.group = group
                        Log.d(
                            "updateWishlistPrivacy",
                            "Nuovo gruppo ${updatedWishlist.group?.groupName} aggiornato con successo"
                        )
                    }
                    val response = wRepository.updateWishlist(updatedWishlist)
                    _wishlists.value =
                        _wishlists.value.map { if (it.id == id) updatedWishlist else it }

                    Log.d("updateWishlistPrivacy", "Wishlist aggiornata con successo")
                    triggerSnackbar("Wishlist aggiornata con successo")

                } else {
                    Log.e("updateWishlistPrivacy", "Wishlist non trovata con id: $id")
                    triggerSnackbar("Errore durante l'aggiornamento della wishlist")

                }
            } catch (e: Exception) {
                Log.e(
                    "updateWishlistPrivacy",
                    "Errore durante l'aggiornamento della privacy della wishlist: ${e.message}"
                )
                triggerSnackbar("Errore durante l'aggiornamento della wishlist")
            }
        }
    }

    fun addWishlistItem(BookId: Long, wishlistId: Long) {
        var message = ""
        viewModelScope.launch {
            try {
                val currentUser = SessionManager.user
                val response =
                    currentUser?.let { wRepository.addWishlistItem(BookId, wishlistId, it.id) }

                if (response != null) {
                    if (response == Unit) {
                        Log.d(
                            "addWishlistItem",
                            "Elemento della wishlist aggiunto con successo"
                        )
                        triggerSnackbar("Elemento della wishlist aggiunto con successo")
                    } else {
                        Log.e(
                            "addWishlistItem",
                            "Errore durante l'aggiunta dell'elemento della wishlist:"
                        )
                        triggerSnackbar("Errore durante l'aggiunta dell'elemento della wishlist")
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "addWishlistItem",
                    "Errore durante l'aggiunta dell'elemento della wishlist: ${e.message}"
                )

                triggerSnackbar("Errore durante l'aggiunta dell'elemento della wishlist")

            }
        }
    }


    fun deleteWishlistItem(id: Long) {
        var message = ""
        viewModelScope.launch {
            try {
                val currentUser = SessionManager.user
                val response = currentUser?.let { wRepository.removeWishlistItem(id, it.id) }
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.d("rimosso WI con id", id.toString())
                        _wishlistItems.value = _wishlistItems.value.filter { it.id != id }
                        Log.d(
                            "removeWishlistItem",
                            "Elemento della wishlist rimosso con successo"
                        )
                        triggerSnackbar("Elemento della wishlist rimosso con successo")
                    } else {
                        Log.e("removeWishlistItem", "Non rimosso WI con id: $id")
                        Log.e(
                            "removeWishlistItem",
                            "Errore durante la rimozione dell'elemento della wishlist: ${response.errorBody()}"
                        )
                        triggerSnackbar("Errore durante la rimozione dell'elemento della wishlist")
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "removeWishlistItem",
                    "Errore durante la rimozione dell'elemento della wishlist: ${e.message}"
                )
                triggerSnackbar("Errore durante la rimozione dell'elemento della wishlist")
            }
        }

    }

    fun deleteWishlist(wishlistId: Long) {
        var message = ""
        viewModelScope.launch {
            try {
                // Elimina la wishlist
                val wResponse = wRepository.removeWishlist(wishlistId)
                if (wResponse.isSuccessful) {
                    _wishlists.value = _wishlists.value.filter { it.id != wishlistId }
                    Log.d("removeWishlist", "Wishlist rimossa con successo")
                    message = "Wishlist rimossa con successo"
                } else {
                    Log.e(
                        "removeWishlist",
                        "Errore durante la rimozione della wishlist: ${wResponse.errorBody()}"
                    )
                    message = "Errore durante la rimozione della wishlist"
                }
                triggerSnackbar(message)
            } catch (e: Exception) {
                Log.e(
                    "removeWishlist",
                    "Errore durante la rimozione della wishlist: ${e.message}"
                )
                message = "Errore durante la rimozione della wishlist"
                triggerSnackbar(message)

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



}

