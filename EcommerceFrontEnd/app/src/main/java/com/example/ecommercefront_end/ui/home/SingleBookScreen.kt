package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
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
import java.time.format.DateTimeFormatter
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.repository.CartRepository
import com.example.ecommercefront_end.ui.books.BookInfoCard
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(book: Book, cartRepository: CartRepository, navController: NavHostController) {
    var selectedQuantity by remember { mutableStateOf(1) }
    var shippingAddress by remember { mutableStateOf("Via Roma 1") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                    text = "di " + book.author,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )
            }
        }

        // Immagine del libro
        item {
            val imageUrl = remember(book.id) {
                testImgs[book.id.hashCode() % testImgs.size]
            }
            val imagePainter = rememberAsyncImagePainter(
                model = imageUrl,
                error = rememberAsyncImagePainter("https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg")
            )
            Image(
                painter = imagePainter,
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
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
                        Text("Quantità: $selectedQuantity")
                    }
                    DropdownMenu(
                        expanded = qExpanded,
                        onDismissRequest = { qExpanded = false }
                    ) {
                        for (i in 1..book.stock) {
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
                text = "Invia a $shippingAddress",
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
                val coroutineScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        coroutineScope.launch {

                            SessionManager.user?.let { user ->
                                cartRepository.addCartItem(user.id, selectedQuantity, book.id)
                            } ?: run {
                                navController.navigate(route = "login") {
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
                    Text("Aggiungi al carrello")
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
                var wExpanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    OutlinedButton(onClick = { wExpanded = true }) {
                        Text("Aggiungi a una wishlist $selectedQuantity")
                    }
                    DropdownMenu(
                        expanded = wExpanded,
                        onDismissRequest = { wExpanded = false }
                    ) {
                        for (i in 1..4) {
                            DropdownMenuItem(
                                onClick = {
                                    selectedQuantity = i
                                    wExpanded = false
                                },
                                text = { Text(text = "$i") }
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
}
