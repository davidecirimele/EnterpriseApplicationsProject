package com.example.ecommercefront_end.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Group
import com.example.ecommercefront_end.model.SaveWishlist
import com.example.ecommercefront_end.model.User
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

class GroupViewModel(private val groupRepository: GroupRepository) : ViewModel() {



    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    private val _groupMembers = MutableStateFlow<List<User>>(emptyList())
    val groupMembers: StateFlow<List<User>> = _groupMembers.asStateFlow()

    private val _userSelectedByAdmin = MutableStateFlow<UUID?>(null)
    val userSelectedByAdmin: StateFlow<UUID?> = _userSelectedByAdmin.asStateFlow()

    private val _isLoading= MutableStateFlow(true)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar: StateFlow<Boolean> get() = _showSnackbar

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> get() = _snackbarMessage

    suspend fun fetchGroups(idUser : UUID? = null) {
        var message = ""
        viewModelScope.launch {

            _isLoading.value = true
            val currentUser = SessionManager.user
            var response: Response<List<Group>>? = null
            try {
                if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                    response = groupRepository.getGroupById(currentUser.id)

                }else if (currentUser != null && currentUser.role == "ROLE_ADMIN") {
                    response = idUser?.let { groupRepository.getGroupById(it) }
                    _userSelectedByAdmin.value = idUser

                }
                if (response != null) {
                    if (response.isSuccessful) {
                        _groups.value = response.body()!!
                        message = "Gruppi recuperati con successo"
                        triggerSnackbar(message)

                    } else {
                        Log.e("fetchGroups", "Errore durante il recupero dei gruppi: ${response.errorBody()}")
                        message = "Errore durante il recupero dei gruppi"
                        triggerSnackbar(message)

                    }
                }
            } catch (e: Exception) {
                Log.e("fetchGroups", "Errore durante il recupero dei gruppi: ${e.message}")
                message = "Errore durante il recupero dei gruppi"
                triggerSnackbar(message)


            }
            _isLoading.value = false
        }
    }

    fun loadGroupMembers(groupId: Long) {
        viewModelScope.launch {
            try {
                fetchGroupMembers(groupId)
            } catch (e: Exception) {
                // Gestione errori, ad esempio mostrando un messaggio di errore
            }
        }
    }

    private suspend fun fetchGroupMembers(idGroup: Long) {
        var message = ""
        viewModelScope.launch {
            val response = groupRepository.getGroupMembersById(idGroup, SessionManager.user?.id!!)
            try {
                if (response.isSuccessful) {
                    _groupMembers.value = response.body()!!
                    message = "Membri del gruppo recuperati con successo"
                } else {
                    Log.e("fetchGroupMembers", "Errore durante il recupero dei membri del gruppo: ${response.errorBody()}")
                    message = "Errore durante il recupero dei membri del gruppo"
                }
            } catch (e: Exception) {
                Log.e("fetchGroupMembers", "Errore durante il recupero dei membri del gruppo: ${e.message}")
                message = "Errore durante il recupero dei membri del gruppo"
            }
        }
        triggerSnackbar(message)
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
                //fetchWishlists(null)// DA SISTEMARE


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

    fun unshareWishlist(groupId: Long) {
        var message = ""
        viewModelScope.launch {
            try {
                val currentUser = SessionManager.user
                var response : Response<Boolean>? = null

                if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                    response = currentUser.let {
                        groupRepository.removeUser(
                            groupId, it.id
                        )
                    }
                } else if (currentUser != null && currentUser.role == "ROLE_ADMIN") {

                    response  = userSelectedByAdmin?.let { it.value?.let { it1 ->
                        groupRepository.removeUser(
                            groupId, it1
                        )
                    } }
                }

                if (response != null && response.isSuccessful) {
                    fetchGroupMembers(groupId)
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

    fun setShowSnackbar(b: Boolean) {
        _showSnackbar.value = b

    }

    fun triggerSnackbar(message: String) {
        _snackbarMessage.value = message
        _showSnackbar.value = true
    }



}