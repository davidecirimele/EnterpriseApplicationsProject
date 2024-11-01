package com.example.ecommercefront_end.ui.wishlist

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager.user
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
import com.example.ecommercefront_end.model.WishlistPrivacy
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.viewmodels.BookViewModel
import com.example.ecommercefront_end.viewmodels.WishlistViewModel


@Composable
fun WishlistsScreen(wishlistViewModel: WishlistViewModel, bookViewModel : BookViewModel, navController: NavController) {

    val wLists by wishlistViewModel.wishlists.collectAsState()
    val wListItems by wishlistViewModel.wishlistItems.collectAsState()
    val isWishlistLoading by wishlistViewModel.isLoadingWishlist.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbar by wishlistViewModel.showSnackbar.collectAsState()
    val snackbarMessage by wishlistViewModel.snackbarMessage.collectAsState()


    // Gestione della selezione della wishlist
    val selectedWishlist = remember(wLists) {
        mutableStateOf(wLists.firstOrNull())
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        paddingValues ->

        if (isWishlistLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Ricarica gli elementi della wishlist selezionata
            LazyColumn(modifier = Modifier.fillMaxSize()
                            .padding(paddingValues))
            {
                item {
                    WishlistsList(
                        wishlists = wLists,
                        viewModel = wishlistViewModel,
                        onWishlistSelected = { wishlist ->
                            selectedWishlist.value = wishlist
                            wishlist.id?.let { wishlistViewModel.fetchWishlistItems(it, user!!.id) }
                        }
                    )
                }

                // Mostra i dettagli solo se c'è una wishlist selezionata e ha degli elementi
                selectedWishlist.value?.let { wishlist ->
                    item {
                        WishlistDetails(
                            wishlist = wishlist,
                            items = wListItems,  // Usa wListItems osservato, non selectedWishlist.items
                            navController = navController,
                            wishlistViewModel = wishlistViewModel,
                            bookViewModel = bookViewModel
                        )
                    }
                }
            }
        }
        LaunchedEffect(showSnackbar) {
            if (showSnackbar) {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    duration = SnackbarDuration.Short
                )
                wishlistViewModel.setShowSnackbar(false) // Resetta lo stato della Snackbar
            }
        }

    }
}


@Composable
fun WishlistsList(wishlists: List<Wishlist>, viewModel: WishlistViewModel, onWishlistSelected: (Wishlist) -> Unit) {
    var showAddWishlistMain by remember { mutableStateOf(false) }
    val isAdmin = user?.role == "ROLE_ADMIN"
    val idUserSelectedByAdmin by viewModel.userSelectedByAdmin.collectAsState()



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isAdmin && (wishlists.get(0).user?.id == idUserSelectedByAdmin) )
                "Liste di ${wishlists.get(0).user?.firstName} ${wishlists.get(0).user?.lastName}" else "Le tue liste dei desideri",

            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { showAddWishlistMain = true }) {
            Icon(imageVector = Icons.Filled.AddCircleOutline, contentDescription = "Aggiungi Lista", tint = Color.Green)
        }


    }
    if (showAddWishlistMain) {
        AddWishlistDialog(
            onDismissRequest = { showAddWishlistMain = false },
            onAddWishlist = { wishlistName, privacySetting -> // wishlistName e isPrivate sono i parametri passati dal dialogo
                viewModel.addWishlist(wishlistName, privacySetting)
                showAddWishlistMain = false
            },
            onJoinWishlist = { token -> // token è il parametro passato dal dialogo
                viewModel.joinWishlist(token)
                showAddWishlistMain = false
            }
        )
    }

    if (wishlists.isEmpty()) { // Controlla se la lista è vuota
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ancora nessuna lista creata.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    } else{
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            items(
                items = wishlists,
                key = { item -> item.id?.toString() ?: "" } // Chiave a livello di items
            ) { wishlist ->
                var isFriendWishlist: Boolean? = null
                if (isAdmin){
                    isFriendWishlist = idUserSelectedByAdmin?.compareTo(wishlist.user?.id) != 0
                }
                else{
                    isFriendWishlist = user?.id?.compareTo(wishlist.user?.id) != 0
                }
                WishlistThumbnail( // Rimuovi il secondo key qui
                    wishlist = wishlist,
                    onClick = { onWishlistSelected(wishlist) },
                    wishlistUpdatable = user?.id?.compareTo(wishlist.user?.id) == 0 || isAdmin,
                    isFriendWishlist = isFriendWishlist

                )
            }
        }
    }
}


@Composable
fun AddWishlistDialog(
    onDismissRequest: () -> Unit,
    onAddWishlist: (String, WishlistPrivacy) -> Unit,
    onJoinWishlist: (String) -> Unit
) {
    var showCreateWishlist by remember { mutableStateOf(false) }
    var wishlistName by remember { mutableStateOf("") }
    var tokenShared by remember { mutableStateOf("") }
    var showJoinWishlist by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedPrivacy by remember { mutableStateOf(WishlistPrivacy.PUBLIC) }
    val privacyOptions = listOf(WishlistPrivacy.PUBLIC, WishlistPrivacy.SHARED, WishlistPrivacy.PRIVATE)


    if (showCreateWishlist) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = "Crea una nuova lista",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            },
            text = {
                Column {
                    // Campo per il nome della wishlist
                    TextField(
                        value = wishlistName,
                        onValueChange = { wishlistName = it },
                        label = { Text("Nome della lista") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selettore per la privacy
                    Button(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Privacy: ${selectedPrivacy.name}")
                    }

                    // DropdownMenu per le opzioni di privacy
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        privacyOptions.forEach { privacy ->
                            DropdownMenuItem(
                                text = { Text(privacy.name) },
                                onClick = {
                                    selectedPrivacy = privacy
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddWishlist(wishlistName, selectedPrivacy)
                        onDismissRequest()
                    }
                ) {
                    Text("Crea")
                }
            },
            dismissButton = {
                Button(onClick = onDismissRequest) {
                    Text("Annulla")
                }
            }

        )
    } else if (showJoinWishlist) {
        AlertDialog(
            onDismissRequest = { showJoinWishlist = false },
            title = {
                Text(
                    "Unisciti alla lista di un amico",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            },
            text = {
                Column {
                    TextField(
                        value = tokenShared,
                        onValueChange = { tokenShared = it },
                        label = { Text("Inserisci il token ricevuto") }
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        onJoinWishlist(tokenShared)
                        showJoinWishlist = false
                    }) {
                        Text("Partecipa")
                    }
                    Button(onClick = { showJoinWishlist = false }) {
                        Text("Annulla")
                    }
                }
            },
            dismissButton = null
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Scegli un'opzione") },
            text = {
                Column {
                    Button(onClick = { showCreateWishlist = true }) {
                        Text("Crea una nuova lista")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { showJoinWishlist = true }) {
                        Text("Unisciti alla lista di un amico")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    Text("Annulla")
                }
            },
            dismissButton = null
        )
    }
}



@Composable
fun WishlistThumbnail(wishlist: Wishlist, onClick: () -> Unit, wishlistUpdatable: Boolean, isFriendWishlist: Boolean) {
    Log.d("WishlistThumbnail", "userId: ${user?.id}, wishlist.userId: ${wishlist.user?.id}")
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(70.dp) // Imposta l'altezza fissa per tutte le card
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (isFriendWishlist){
                Color.Cyan
            }
            else{
                Color.LightGray
            }
        ) // Colore di riempimento condizionale
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = wishlist.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    //.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Aggiungi eventuali altri dettagli della wishlist qui
        }
    }
}





