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
fun WishlistDetails(
    wishlist: Wishlist,
    items: List<WishlistItem>,
    navController: NavController,
    wishlistViewModel: WishlistViewModel,
    bookViewModel: BookViewModel
) {

    var privacyOptions by remember { mutableStateOf(wishlist.privacySetting) }
    var showMenu by remember { mutableStateOf(false) } // Per gestire la visibilità del menu a comparsa

    val isAdmin = user?.role == "ROLE_ADMIN"
    val wishlistUpdatable = user?.id?.compareTo(wishlist.user?.id) == 0 || isAdmin
    var isFriendWishlist : Boolean? = null

    if (isAdmin){
        val idUserSelectedByAdmin by wishlistViewModel.userSelectedByAdmin.collectAsState()
        isFriendWishlist = idUserSelectedByAdmin?.compareTo(wishlist.user?.id) != 0

    }
    else{
        isFriendWishlist = user?.id?.compareTo(wishlist.user?.id) != 0
    }

    // YTk4YzJiYzQtMGQ5NS00YjI0LThjODQtMzE3N2RjNTkyODVl

    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var itemToRemove by remember { mutableStateOf<WishlistItem?>(null) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var newWishlistName by remember { mutableStateOf(wishlist.name) }

    val isLoadingWishlist by wishlistViewModel.isLoadingWishlist.collectAsState()
    val isLoadingItems by wishlistViewModel.isLoadingItems.collectAsState()
    val error by wishlistViewModel.error.collectAsState()

    val tokenToShare by wishlistViewModel.tokenToShare.collectAsState()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // Ricarica gli elementi della wishlist selezionata
    LaunchedEffect(key1 = wishlist.id) {
        wishlist.id?.let { user?.let { it1 -> wishlistViewModel.fetchWishlistItems(it, it1.id) } }
    }

    if (isLoadingWishlist) {
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
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = "${wishlist.name}",
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
                        if(wishlistUpdatable){
                            DropdownMenuItem(
                                text = { Text("Condividi") },
                                onClick = {
                                    showMenu = false
                                    // Azione per condividere la wishlist
                                    val token = wishlist.wishlistToken
                                    clipboardManager.setText(AnnotatedString(token))
                                    Log.d("WishlistDetails", "Token da copiare: $token")
                                    Toast.makeText(context, "Token copiato negli appunti!", Toast.LENGTH_SHORT).show()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Share,
                                        contentDescription = "Condividi",
                                        tint = Color.Blue
                                    )
                                }
                            )
                        }
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
                            text = { Text(if (wishlistUpdatable) "Elimina" else "Esci") },
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
                        title = { Text(if (wishlistUpdatable) "Elimina Wishlist" else "Esci") },
                        text = { Text("Vuoi davvero uscire dalla wishlist '${wishlist.name} ${if (!wishlistUpdatable) "di" else "da"} ${wishlist.user?.firstName}'?") },
                        confirmButton = {
                            Button(onClick = {
                                //onDeleteWishlist(wishlist) // Chiama il callback per eliminare la wishlist
                                if(wishlistUpdatable){
                                    wishlist.id?.let { wishlistViewModel.deleteWishlist(it) }
                                }
                                else{
                                    wishlistViewModel.unshareWishlist(wishlist)
                                }
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
                                wishlist.id?.let { wishlistViewModel.updateWishlist(it, newWishlistName, privacyOptions, null) } // Chiama il callback per rinominare la wishlist
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

            //Proprietario
            if (isFriendWishlist){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Condivisa da: ${wishlist.user?.firstName} ${wishlist.user?.lastName}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            // Sezione Privacy
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
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
                        enabled = wishlistUpdatable,
                        onClick = {
                            var privacySetting = wishlist.privacySetting
                            if (wishlist.privacySetting == WishlistPrivacy.PRIVATE)
                                privacySetting = WishlistPrivacy.SHARED

                            else if (wishlist.privacySetting == WishlistPrivacy.SHARED)
                                privacySetting = WishlistPrivacy.PUBLIC

                            else if (wishlist.privacySetting == WishlistPrivacy.PUBLIC)
                                privacySetting = WishlistPrivacy.PRIVATE

                            wishlist.id?.let { wishlistViewModel.updateWishlist(it, "", privacySetting, null) }
                        },
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector =
                            if (wishlist.privacySetting == WishlistPrivacy.PRIVATE)
                                Icons.Rounded.Lock

                            else if (wishlist.privacySetting == WishlistPrivacy.SHARED)
                                Icons.Rounded.Share

                            else Icons.Rounded.LockOpen,

                            contentDescription = "Cambia Privacy",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                        Text("${wishlist.privacySetting.name}",)
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            if (items.isNotEmpty()) {
                if (isLoadingItems) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    Column {
                        items.forEach { item ->

                            WishlistItemCard(wishlistItem = item,
                                navController = navController, onRemoveClick = {
                                    wishlistViewModel.deleteWishlistItem(item.id)
                                },
                                bookViewModel = bookViewModel,
                                wishlistUpdateable = wishlistUpdatable,
                                isFriendWishlist = isFriendWishlist
                            )
                        }
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
    /*
    if (tokenToShare.isNotEmpty()) { // TO DO : Aggiungere un controllo per verificare se il token è già stato copiato
        clipboardManager.setText(AnnotatedString(tokenToShare))
        Toast.makeText(context, "Token copiato negli appunti!", Toast.LENGTH_SHORT).show()
        // Pulisci il tokenToShare dopo che è stato copiato
        //viewModel.clearToken()
    }*/
}

@Composable
fun WishlistItemCard(
    bookViewModel: BookViewModel,
    wishlistItem: WishlistItem,
    navController: NavController,
    onRemoveClick: () -> Unit,
    wishlistUpdateable: Boolean,
    isFriendWishlist: Boolean

) {
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
                    .size(70.dp, 90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                BookCover(
                    book = wishlistItem.book,
                    viewModel = bookViewModel
                )
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

            IconButton(onClick = onRemoveClick, enabled = wishlistUpdateable) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Rimuovi elemento",tint = if (wishlistUpdateable) Color.Red  else Color.Gray)
            }
        }
    }
}