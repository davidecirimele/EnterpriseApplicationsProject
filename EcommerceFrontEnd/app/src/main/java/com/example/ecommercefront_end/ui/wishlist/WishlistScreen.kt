package com.example.ecommercefront_end.ui.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun WishlistsList(wishlists: List<Wishlist>, onWishlistSelected: (Wishlist) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = wishlist.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Aggiungi eventuali altri dettagli della wishlist qui
        }
    }
}

@Composable
fun WishlistDetails(wishlist: Wishlist, items: List<WishlistItem>, navController: NavController) {
    var isPrivate by remember { mutableStateOf(wishlist.privacySetting == "Private") }


    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Lista: ${wishlist.name}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Privacy: ",
                fontSize = 18.sp
            )
            // Bottonecon icona e testo
            Button(
                onClick = { isPrivate = !isPrivate },
                modifier = Modifier.height(36.dp), // Altezza del bottone
                contentPadding = PaddingValues(horizontal = 12.dp) // Padding interno
            ) {
                Icon(
                    imageVector = if (isPrivate) Icons.Rounded.Lock
                                 else Icons.Rounded.LockOpen,
                    contentDescription = "Cambia Privacy",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(if (isPrivate) "Private" else "Public")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        LazyColumn {
            items(items) { item ->
                WishlistItemCard(wishlistItem = item, navController = navController)
            }
        }
    }
}

@Composable
fun WishlistItemCard(wishlistItem: WishlistItem, navController: NavController) {
    var quantity by remember { mutableStateOf(wishlistItem.quantity) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = wishlistItem.book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Di ${wishlistItem.book.author}",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${"%,.2f".format(wishlistItem.book.price)} €",
                    fontSize = 16.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Icon(
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = "Riduci Quantità"
                    )
                }
                Text(
                    text = "$quantity",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = { quantity++ }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Incrementa Quantità"
                    )
                }
            }
        }
    }
}


