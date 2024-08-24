package com.example.ecommercefront_end.ui.wishlist

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.model.WishlistItem
import com.example.ecommercefront_end.viewmodels.WishlistViewModel

@Composable
fun WishlistsScreen(viewModel: WishlistViewModel, navController: NavController) {
    val wLists by viewModel.wishlists.collectAsState()
    val wListItems by viewModel.wishlistItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Gestione della selezione della wishlist
    val selectedWishlist = remember(wLists) {
        mutableStateOf(wLists.firstOrNull())
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Ricarica gli elementi della wishlist selezionata
        LaunchedEffect(selectedWishlist.value) {
            selectedWishlist.value?.let {
                viewModel.loadWishlistItemsFromDB(it.id)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                WishlistsList(
                    wishlists = wLists,
                    viewModel = viewModel,
                    onWishlistSelected = { wishlist ->
                        selectedWishlist.value = wishlist
                        viewModel.loadWishlistItemsFromDB(wishlist.id)
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
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}


@Composable
fun AddWishlistDialog(onDismissRequest: () -> Unit, onAddWishlist: (String, Boolean) -> Unit) {
    var wishlistName by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    var otherParam by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Crea una nuova lista") },
        text = {
            Column {
                TextField(
                    value = wishlistName,
                    onValueChange = { wishlistName = it },
                    label = { Text("Nome della lista") }
                )
                Row {
                    Checkbox(
                        checked = isPrivate,
                        onCheckedChange = { isPrivate = it }
                    )
                    Text(text = "Privata", modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround // Centra i bottoni
            ) {
                Button(onClick = { onAddWishlist(wishlistName, isPrivate) }) {
                    Text("Crea")
                }
                Button(onClick = onDismissRequest) {
                    Text("Annulla")
                }
            }
        },
        dismissButton = null // Non è più necessario
        //... (altri parametri)
    )
}

@Composable
fun WishlistsList(wishlists: List<Wishlist>, viewModel: WishlistViewModel, onWishlistSelected: (Wishlist) -> Unit) {
    var showAddWishlist by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Le tue liste desideri",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { showAddWishlist = true }) {
            Icon(imageVector = Icons.Filled.AddCircleOutline, contentDescription = "Aggiungi Lista", tint = Color.Green)
        }
    }
    if (showAddWishlist) {
        AddWishlistDialog(
            onDismissRequest = { showAddWishlist = false },
            onAddWishlist = { wishlistName, isPrivate ->
                viewModel.addWishlist(wishlistName, isPrivate)// Crea la lista con più parametri
                showAddWishlist = false
            }
        )
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items(wishlists) { wishlist ->
            WishlistThumbnail(
                wishlist = wishlist,
                onClick = { onWishlistSelected(wishlist) }
            )
        }
    }
}

@Composable
fun WishlistThumbnail(wishlist: Wishlist, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .height(70.dp) // Imposta l'altezza fissa per tutte le card
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Aggiungi eventuali altri dettagli della wishlist qui
        }
    }
}

@Composable
fun WishlistDetails(
    wishlist: Wishlist,
    items: List<WishlistItem>,
    navController: NavController,
    viewModel: WishlistViewModel
) {
    var isPrivate by remember { mutableStateOf(wishlist.privacySetting == "Private") }
    var showMenu by remember { mutableStateOf(false) } // Per gestire la visibilità del menu a comparsa

    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var itemToRemove by remember { mutableStateOf<WishlistItem?>(null) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var newWishlistName by remember { mutableStateOf(wishlist.name) }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Ricarica gli elementi della wishlist selezionata
    LaunchedEffect(wishlist) {
        viewModel.loadWishlistItemsFromDB(wishlist.id)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else {
        Column(modifier = Modifier.padding(16.dp)) {
            // Titolo della lista
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Lista: ${wishlist.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Menu a comparsa
                Box{
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu", tint = Color.Blue)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Condividi") },
                            onClick = {
                                showMenu = false
                                // Azione per condividere la wishlist
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Condividi",
                                    tint = Color.Blue
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Rinomina") },
                            onClick = {
                                showMenu = false
                                // Azione per rinominare la wishlist
                                showRenameDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Rinomina",
                                    tint = Color.Blue
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Elimina") },
                            onClick = {
                                showMenu = false
                                // Azione per eliminare la wishlist
                                showDeleteConfirmation = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Elimina",
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
                if (showDeleteConfirmation) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmation = false },
                        title = { Text("Elimina Wishlist") },
                        text = { Text("Vuoi davvero eliminare la wishlist '${wishlist.name}'?") },
                        confirmButton = {
                            Button(onClick = {
                                //onDeleteWishlist(wishlist) // Chiama il callback per eliminare la wishlist
                                viewModel.removeWishlist(wishlist.id)
                                showDeleteConfirmation = false
                            }) {
                                Text("Sì")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteConfirmation = false }) {
                                Text("No")
                            }
                        }
                    )
                }

                // Dialogo di rinomina
                if (showRenameDialog) {
                    AlertDialog(
                        onDismissRequest = { showRenameDialog = false },
                        title = { Text("Rinomina Wishlist") },
                        text = {
                            OutlinedTextField(
                                value = newWishlistName,
                                onValueChange = { newWishlistName = it },
                                label = { Text("Nuovo nome") }
                            )
                        },
                        confirmButton = {
                            Button(onClick = {
                                viewModel.updateWishlist(wishlist.id, newWishlistName, "", null) // Chiama il callback per rinominare la wishlist
                                showRenameDialog = false
                            }) {
                                Text("Rinomina")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showRenameDialog = false }) {
                                Text("Annulla")
                            }
                        }
                    )
                }
            }
            // Sezione Privacy
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Privacy text e bottone per modificare lo stato di privacy
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Privacy: ",
                        fontSize = 18.sp
                    )
                    Button(
                        onClick = {
                            isPrivate = !isPrivate
                            if (isPrivate)
                                viewModel.updateWishlist(wishlist.id, "", "Private", null)
                            else
                                viewModel.updateWishlist(wishlist.id, "", "Public", null)
                        },
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = if (isPrivate) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
                            contentDescription = "Cambia Privacy",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(if (isPrivate) "Private" else "Public")
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            if (!items.isNullOrEmpty()) {
                Column {
                    items.forEach { item ->
                        WishlistItemCard(wishlistItem = item,
                            navController = navController, onRemoveClick = {
                                viewModel.removeWishlistItem(item.id)
                            })
                    }
                }
            }
            else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Errore durante il caricamento degli elementi: ${error.toString()}")
                }
            }
        }

    }

}

@Composable
fun WishlistItemCard(wishlistItem: WishlistItem, navController: NavController,onRemoveClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.Gray)
            ) {
                // Inserisci l'immagine del libro qui
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = wishlistItem.book.title,
                    fontSize= 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Di ${wishlistItem.book.author}",
                    fontSize = 14.sp
                )
                Text(
                    text = "${"%,.2f".format(wishlistItem.book.price)} €",
                    fontSize = 16.sp
                )
            }

            IconButton(onClick = onRemoveClick) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Rimuovi elemento",tint = Color.Red)
            }
        }
    }
}


