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
import kotlinx.coroutines.cancel
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

                    } else {
                        Log.e("fetchGroups", "Errore durante il recupero dei gruppi: ${response.errorBody()}")
                        message = "Error during fetching groups: ${response.errorBody()}"
                        triggerSnackbar(message)

                    }
                }
            } catch (e: Exception) {
                Log.e("fetchGroups", "Errore durante il recupero dei gruppi: ${e.message}")
                message = "Error during fetching groups: ${e.message}"
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
                Log.e("loadGroupMembers", "Error during fetching group members: ${e.message}")
                triggerSnackbar("Error during fetching group members: ${e.message}")
            }
        }
    }

    private suspend fun fetchGroupMembers(idGroup: Long) {
        val response = groupRepository.getGroupMembersById(idGroup, SessionManager.user?.id!!)
        if (response.isSuccessful) {
            _groupMembers.value = response.body()!! // Aggiorna direttamente _groupMembers
            Log.d("fetchGroupMembers", _groupMembers.value.toString())
        } else {
            val errorMsg = "Error during fetching group members: ${response.errorBody()}"
            Log.e("fetchGroupMembers", errorMsg)
        }
    }



    fun joinWishlist(token: String) {
        viewModelScope.launch {
            var message = ""
            try {
                val currentUser = SessionManager.user
                var response : Response<Int>? = null

                if (currentUser != null && currentUser.role != "ROLE_ADMIN") {
                    Log.d("joinWishlist", "primo if, idUser = ${currentUser.id}")
                    response = currentUser?.let { groupRepository.addUser(it.id, token) }

                } else if (currentUser != null && currentUser.role == "ROLE_ADMIN") {
                    Log.d("joinWishlist", "secondo if, idUser = ${currentUser.id} ${_userSelectedByAdmin}")

                    response = _userSelectedByAdmin.value?.let { groupRepository.addUser(it, token) }

                }
                //fetchWishlists(null)// DA SISTEMARE

                if (response != null) {
                    if (response.body() == 0 || response.body() == 1) {
                        Log.d("joinWishlist", "User added to wishlist successfully")
                        message = "You joined the wishlist successfully"
                    } else {
                        //Log.e("joinWishlist", "Errore durante l'aggiunta dell'utente alla wishlist: ${response.errorBody()}")
                        message = "Erro during joining the wishlist: + ${response.errorBody()}"
                    }
                }
                triggerSnackbar(message)

            } catch (e: Exception) {
                Log.e(
                    "joinWishlist",
                    "Error during adding user to wishlist: ${e.message}"
                )
                message = "Error during adding user to wishlist ${e.message}"
                triggerSnackbar(message)

            }
        }
    }

    fun unshareWishlist(groupId: Long, memeberId: UUID) {
        viewModelScope.launch {
            var message = ""
            try {
                val currentUser = SessionManager.user
                val response: Response<Boolean>? = when {
                    currentUser != null && currentUser.role != "ROLE_ADMIN" -> {
                        groupRepository.removeUser(groupId, memeberId, currentUser.id)
                    }
                    currentUser != null && currentUser.role == "ROLE_ADMIN" -> {
                        userSelectedByAdmin.value?.let { selectedUserId ->
                            groupRepository.removeUser(groupId, memeberId, selectedUserId)
                        }
                    }
                    else -> null
                }

                if (response != null && response.isSuccessful) {
                    // Aggiorna i membri dopo aver rimosso l'utente
                    fetchGroupMembers(groupId)
                    Log.d("unshareWishlist", "Wishlist non più condivisa con successo")
                    message = "Wishlist not shared anymore"
                } else {
                    message = "Error during unsharing wishlist: ${response?.errorBody()}"
                    Log.e("unshareWishlist", message)
                }
                triggerSnackbar(message)
            } catch (e: Exception) {
                message = "Error during unsharing wishlist: ${e.message}"
                Log.e("unshareWishlist", message)
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

    fun onLogout(){
        //viewModelScope.cancel()
        _groups.value = emptyList()
        _groupMembers.value = emptyList()
        _userSelectedByAdmin.value = null
    }

}