package com.example.ecommercefront_end.ui.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    // Stato per tracciare le liste e gli items
    val wLists by viewModel.wishlists.collectAsState()
    val wListItems by viewModel.wishlistItems.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Stato per tracciare la wishlist selezionata
    val (selectedWishlist, setSelectedWishlist) = remember(wLists) {
        mutableStateOf(wLists.firstOrNull())
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Carica gli items della prima wishlist alla creazione della composizione
        LaunchedEffect(wLists) {
            selectedWishlist?.let { viewModel.loadWishlistItemsFromDB(it.id) }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Galleria di Wishlist
            LazyRow(modifier = Modifier.padding(8.dp)) {
                items(wLists) { wishlist ->
                    WishlistThumbnail(
                        wishlist = wishlist,
                        onClick = {
                            setSelectedWishlist(wishlist)
                            viewModel.loadWishlistItemsFromDB(wishlist.id)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostra i dettagli della wishlist selezionata e gli items
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
        }
    }
}

@Composable
fun WishlistDetails(wishlist: Wishlist, items: List<WishlistItem>, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Wishlist: ${wishlist.name}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Privacy: ${wishlist.privacySetting}",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Lista degli items nella wishlist
        WishlistItemsList(items = items, navController = navController)
    }
}

@Composable
fun WishlistItemsList(items: List<WishlistItem>, navController: NavController) {
    LazyColumn {
        items(items) { item ->
            WishlistItemCard(wishlistItem = item, navController = navController)
        }
    }
}

@Composable
fun WishlistItemCard(wishlistItem: WishlistItem, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                // Naviga ai dettagli del libro o altro
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder per un'immagine del libro
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = wishlistItem.book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Author: ${wishlistItem.book.author}",
                    fontSize = 14.sp
                )
            }
        }
    }
}

