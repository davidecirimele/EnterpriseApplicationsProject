package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import com.example.ecommercefront_end.viewmodels.HomeViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.ecommercefront_end.model.Book

@Composable
fun BookDetailsScreen(book: Book) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Immaginedel libro
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
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        // Dettagli del libro
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = book.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "di ${book.author}", fontSize = 16.sp)
        Text(text = "Prezzo: ${book.price} €", fontSize = 16.sp)
        // ... altri dettagli del libro come ISBN, pagine, genere, ecc.
    }
}