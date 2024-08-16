package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.viewmodels.HomeViewModel


@Composable
fun ProductCard(book: Book) {
    var fakeUrl: String = "https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg"
    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = fakeUrl,
                    error = rememberAsyncImagePainter("https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg")), // Assicurati che Book abbia un campo imageUrl
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = book.title, fontWeight = FontWeight.Bold)
            Text(text = book.author)
            Text(text = "€${book.price}") // Assicurati che Book abbia un campo price
        }
    }
}

@Composable
fun ProductSection(title: String, books: List<Book>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(books) { book ->
                ProductCard(book)
            }
        }
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val products by homeViewModel.products.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()

    if (isLoading) {
        // Mostra un indicatore di caricamento
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        // Mostra un messaggio di errore
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Errore: $error")
        }
    } else {
        println("Numero di libri caricati: ${products.size}") // Aggiungi un log per vedere quanti libri sono stati caricati
        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nessun prodotto disponibile")
            }
        } else {
            // Mostra i prodotti
            LazyColumn {
                item {
                    // Chiama ProductSection per mostrare una sezione con il titolo "Libri disponibili"
                    ProductSection(title = "Libri disponibili", books = products)
                }
            }
        }
    }

}
