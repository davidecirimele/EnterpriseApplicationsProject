package com.example.ecommercefront_end.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.ecommercefront_end.viewmodels.HomeViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.SessionManager.user
import java.time.format.DateTimeFormatter
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.Wishlist
import com.example.ecommercefront_end.repository.CartRepository
import com.example.ecommercefront_end.repository.WishlistRepository
import com.example.ecommercefront_end.viewmodels.WishlistViewModel
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.ui.books.BookInfoCard
import com.example.ecommercefront_end.viewmodels.BookViewModel
import com.example.ecommercefront_end.viewmodels.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(book: Book, bookViewModel: BookViewModel, cartViewModel: CartViewModel, wishlistViewModel: WishlistViewModel, navController: NavHostController) {
    var selectedQuantity by remember { mutableStateOf(1) }
    var shippingAddress by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var selectedWishlist by remember { mutableStateOf<Wishlist?>(null)}
    val userWishlist by wishlistViewModel.onlyMyWishlists.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val wShowSnackbar by wishlistViewModel.showSnackbar.collectAsState()
    val wSnackbarMessage by wishlistViewModel.snackbarMessage.collectAsState()





    var wExpanded by remember { mutableStateOf(false) }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
            paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            // Titolo e descrizione allineati a sinistra
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = book.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        text = book.author,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                }
            }

            // Immagine del libro
            item {
                BookCover(book, bookViewModel, navController)
            }

            // Prezzo e quantità: prezzo allineato a sinistra, quantità a destra
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "€ ${"%,.2f".format(book.price)}",
                        fontSize = 31.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    var qExpanded by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        OutlinedButton(onClick = { qExpanded = true }) {
                            Text("Quantity: $selectedQuantity")
                        }
                        DropdownMenu(
                            expanded = qExpanded,
                            onDismissRequest = { qExpanded = false }
                        ) {
                            var quantityToShow = 6
                            if (book.stock < 6) {
                                quantityToShow = book.stock
                            }
                            for (i in 1..quantityToShow) {
                                DropdownMenuItem(
                                    onClick = {
                                        selectedQuantity = i
                                        qExpanded = false
                                    },
                                    text = { Text(text = "$i") }
                                )
                            }
                        }
                    }
                }
            }
            // Indirizzo di spedizione
            item {
                Text(
                    text = "Send to $shippingAddress",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Start
                )
            }


            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Pulsanti Aggiungi al carrello e Acquista subito
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {

                                SessionManager.user?.let { user ->
                                    cartViewModel.addItem( book)
                                } ?: run {
                                    navController.navigate(route = "userAuth") {
                                        popUpTo(route = "cart") {
                                            inclusive = true
                                        }
                                    }
                                }


                            }

                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(bottom = 18.dp)
                    ) {
                        Text("Add to cart")
                    }
                }
            }


            item { Spacer(modifier = Modifier.height(25.dp)) }
            // Wishlist
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        OutlinedButton(onClick = {
                            if (user == null) {
                                navController.navigate("userAuth"){
                                    popUpTo("wishlist") {
                                        inclusive = true
                                    }
                                }
                            }
                            else{
                                if (userWishlist.isEmpty()) {
                                    wishlistViewModel.triggerSnackbar("Non hai ancora creato nessuna wishlist")
                                    navController.navigate("wishlist")
                                }
                                else {
                                    wExpanded = true
                                }
                            }
                        }
                        ) {
                            Text("Aggiungi a una wishlist ")
                        }
                        DropdownMenu(
                            expanded = wExpanded,
                            onDismissRequest = { wExpanded = false }
                        ) {
                            Log.d("Aggiung item", userWishlist.toString())
                            userWishlist.forEach { w ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedWishlist = w
                                        wExpanded = false

                                        w.id?.let {
                                            wishlistViewModel.addWishlistItem(
                                                book.id,
                                                w.id
                                            )
                                        }
                                    },
                                    text = { Text(w.name) }
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            item {
                BookInfoCard(book)
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


            }
        }
    }

