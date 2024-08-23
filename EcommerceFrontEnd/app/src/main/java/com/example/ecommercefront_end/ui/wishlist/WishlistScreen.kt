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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Remove
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
    val error by viewModel.error.collectAsState()

    var showAddWishlist by remember { mutableStateOf(false) }

    val (selectedWishlist, setSelectedWishlist) = remember(wLists) {
        mutableStateOf(wLists.firstOrNull())
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = error.toString())
        }
    } else {
        LaunchedEffect(wLists) {
            selectedWishlist?.let { viewModel.loadWishlistItemsFromDB(it.id) }
        }

        Column(modifier = Modifier.fillMaxSize()) {
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
                    Icon(imageVector = Icons.Filled.AddCircleOutline, contentDescription = "Aggiungi Lista")
                }
            }
            if (showAddWishlist) {
                AddWishlistDialog(
                    onDismissRequest = { showAddWishlist = false },
                    onAddWishlist = { wishlistName, isPrivate, otherParam ->
                        viewModel.addWishlist(wishlistName, isPrivate, otherParam)// Crea la lista con più parametri
                        showAddWishlist = false
                    }
                )
            }
            WishlistsList(
                wishlists = wLists,
                onWishlistSelected = { wishlist ->
                    setSelectedWishlist(wishlist)
                    viewModel.loadWishlistItemsFromDB(wishlist.id)
                }
            )

            selectedWishlist?.let { wishlist ->
                WishlistDetails(
                    wishlist = wishlist,
                    items = wListItems,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun AddWishlistDialog(onDismissRequest: () -> Unit, onAddWishlist: (String, Boolean, String) -> Unit) {
    var wishlistName by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    var otherParam by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Crea una nuova lista desideri") },
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
                    Text("Privata")
                }
                TextField(
                    value = otherParam,
                    onValueChange = { otherParam = it },
                    label = { Text("Altro parametro") }
                )
                // Aggiungi altri campi per i dettagli della wishlist se necessario
            }
        },
        confirmButton = {
            Button(onClick = { onAddWishlist(wishlistName, isPrivate, otherParam) }) {
                Text("Crea")}
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Annulla")
            }
        }
    )
}

@Composable
fun WishlistsList(wishlists: List<Wishlist>, onWishlistSelected: (Wishlist) -> Unit) {

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
fun WishlistDetails(wishlist: Wishlist, items: List<WishlistItem>, navController: NavController) {
    var isPrivate by remember { mutableStateOf(wishlist.privacySetting == "Private") }
    var showMenu by remember { mutableStateOf(false) } // Per gestire la visibilità del menu a comparsa

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
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Condividi Wishlist") },
                        onClick = {
                            showMenu = false
                            // Azione per condividere la wishlist
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Condividi Wishlist"
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Rinomina Wishlist") },
                        onClick = {
                            showMenu = false
                            // Azione per rinominare la wishlist
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Rinomina Wishlist"
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Elimina Wishlist") },
                        onClick = {
                            showMenu = false
                            // Azione per eliminare la wishlist
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Elimina Wishlist"
                            )
                        }
                    )
                }
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
                    onClick = { isPrivate = !isPrivate },
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
        LazyColumn {
            items(items) { item ->
                WishlistItemCard(wishlistItem = item, navController = navController,onRemoveClick = {
                    // Gestisci la rimozione dell'elemento qui, ad esempio chiamando una funzione nel tuo viewModel
                })
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
                .padding(8.dp).fillMaxWidth(),
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
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Rimuovi elemento")
            }
        }
    }
}


