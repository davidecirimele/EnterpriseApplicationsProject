package com.example.ecommercefront_end.ui.wishlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager.user
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistPrivacy
import com.example.ecommercefront_end.viewmodels.BookViewModel
import com.example.ecommercefront_end.viewmodels.CartViewModel
import com.example.ecommercefront_end.viewmodels.WishlistViewModel


@Composable
fun WishlistsScreen(wishlistViewModel: WishlistViewModel, bookViewModel : BookViewModel, cartViewModel: CartViewModel, navController: NavController) {

    val wLists by wishlistViewModel.wishlists.collectAsState()
    val wListItems by wishlistViewModel.wishlistItems.collectAsState()
    val isWishlistLoading by wishlistViewModel.isLoadingWishlist.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val wShowSnackbar by wishlistViewModel.showSnackbar.collectAsState()
    val wSnackbarMessage by wishlistViewModel.snackbarMessage.collectAsState()

    val cartShowSnackbar by cartViewModel.showSnackbar.collectAsState()
    val cartSnackbarMessage by cartViewModel.snackbarMessage.collectAsState()




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
                            cartViewModel = cartViewModel,
                            bookViewModel = bookViewModel
                        )
                    }
                }
            }
        }
        LaunchedEffect(wShowSnackbar) {
            if (wShowSnackbar) {
                snackbarHostState.showSnackbar(
                    message = wSnackbarMessage,
                    duration = SnackbarDuration.Short
                )
                wishlistViewModel.setShowSnackbar(false) // Resetta lo stato della Snackbar
            }
                cartViewModel.setShowSnackbar(false)

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
            Icon(imageVector = Icons.Filled.AddCircleOutline, contentDescription = "Aggiungi Lista", tint = Color.Green, modifier = Modifier.size(28.dp))
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
    var nameWishlistValid by remember { mutableStateOf(false) }

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
                    modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)
                )
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo per il nome della wishlist
                        OutlinedTextField(
                            value = wishlistName,
                            onValueChange = {
                                wishlistName = it
                                nameWishlistValid = it.isNotBlank()

                            },
                            label = { Text("Nome della lista") },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        // Selettore per la privacy
                        Button(
                            onClick = { expanded = !expanded },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(bottom = 8.dp),
                        ) {
                            Text("Privacy: ${selectedPrivacy.name}")
                        }

                        // DropdownMenu per le opzioni di privacy
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.wrapContentSize()
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
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        onAddWishlist(wishlistName, selectedPrivacy)
                        onDismissRequest()
                    },
                        enabled = nameWishlistValid // Abilita il pulsante solo se il campo non è vuoto
                    ) {
                        Text("Crea")
                    }
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        )
                    {
                        Text("Annulla")

                    }
                }
            },
            dismissButton = null,
            modifier = Modifier.fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .heightIn(max = 300.dp)
        )
    }else if (showJoinWishlist) {
        AlertDialog(
            onDismissRequest = { showJoinWishlist = false },
            title = {
                // Centra il titolo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "Unisciti alla lista di un amico",
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                // Centra il campo di testo
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = tokenShared,
                        onValueChange = { tokenShared = it },
                        label = { Text("Inserisci il token ricevuto") },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                    )
                }
            },
            confirmButton = {
                // Centra i pulsanti
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Aggiungi spazio tra i pulsanti
                ) {
                    Button(
                        onClick = {
                            onJoinWishlist(tokenShared)
                            showJoinWishlist = false
                        }
                    ) {
                        Text("Partecipa")
                    }
                    Button(
                        onClick = { showJoinWishlist = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Annulla")
                    }
                }
            },
            dismissButton = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .heightIn(max = 300.dp)
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            text = {
                Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row( // Aggiunta Row per i due pulsanti principali
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center // Centra i pulsanti nella Row
                        ) {
                            Button(
                                onClick = { showCreateWishlist = true },
                                modifier = Modifier.weight(1f)
                                    .width(190.dp) // Larghezza uniforme
                                    .height(75.dp) // Altezza uniforme
                                    .padding(horizontal = 2.dp),
                            ) {
                                Text("Crea una nuova lista")
                            }

                            Spacer(modifier = Modifier.width(8.dp)) // Spazio tra i pulsanti

                            Button(
                                onClick = { showJoinWishlist = true },
                                modifier = Modifier.weight(1f)
                                    .width(190.dp)
                                    .height(75.dp) // Altezza uniforme
                                    .padding(horizontal = 4.dp)
                            ) {
                                Text("Unisciti alla lista di un amico")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp)) // Spazio tra i pulsanti e "Annulla"
                    }
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { // Box per centrare "Annulla"
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Annulla")
                    }
                }
            },

            dismissButton = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .heightIn(max = 220.dp)
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
            if (isFriendWishlist) {
                Color.Cyan
            } else {
                Color.LightGray
            }
        ), // Colore di riempimento condizionale

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = wishlist.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center
                //.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Aggiungi eventuali altri dettagli della wishlist qui
        }
    }
}





