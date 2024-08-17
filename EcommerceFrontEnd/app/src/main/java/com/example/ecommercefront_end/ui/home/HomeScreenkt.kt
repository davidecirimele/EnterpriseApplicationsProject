package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.viewmodels.HomeViewModel


@Composable
fun ProductCard(book: Book, height: Dp, width: Dp ) {
    var fakeUrl: String = "https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg"
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(width),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = fakeUrl,error = rememberAsyncImagePainter("https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg")
                ),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = book.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
            Text(text = book.author, fontSize = 12.sp, maxLines = 1)
            Text(text = "€${book.price}", fontSize = 12.sp)
        }
    }
}


@Composable
fun ProductSection(title: String, books: List<Book>, height: Dp = 200.dp, width: Dp = 100.dp) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(books) { book ->
                ProductCard(book, height, width) // Aumentiamo la larghezza delle card (150 dp invece di 100 dp)
            }
        }
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val products by homeViewModel.products.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()

    // Log per verificare il numero di libri caricati
    println("Numero di libri caricati: ${products.size}")

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Errore: $error")
        }
    } else {
        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nessun prodotto disponibile")
            }
        } else {
            // Aggiunto un colore di sfondo per debug visivo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(max = 600.dp)
                    .padding(16.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Sezione di test con dati statici per vedere se funziona
                    item {
                        ProductSection(
                            title = "Libri Popolari",
                            books = products,
                            height = 250.dp, // Altezze maggiori per visibilità
                            width = 150.dp // Aumentiamo la larghezza delle card
                        )
                    }
                }
            }
        }
    }
}




