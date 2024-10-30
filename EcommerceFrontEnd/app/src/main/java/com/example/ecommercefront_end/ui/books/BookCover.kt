package com.example.ecommercefront_end.ui.books

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.BookGenreColor
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.utils.BookCoverPlaceholder
import com.example.ecommercefront_end.viewmodels.BookViewModel


@Composable
fun BookCover(book: Book, viewModel: BookViewModel) {
    var loadingImage by remember { mutableStateOf(true) }
    val imageUrl = book.imagePath

    var image by remember { mutableStateOf<Bitmap?>(null) }

    val gold = 0xFFFFD700.toInt()
    val darkblue = 0xFF0B3D91.toInt()
    val darkgreen = 0xFF2E8B57.toInt()
    val bordeaux = 0xFF800020.toInt()
    val purple = 0xFF4B0082.toInt()
    val orange = 0xFFFF8C00.toInt()
    val antracyte = 0xFF2F4F4F.toInt()

    val coverColors = listOf(
        BookGenreColor(BookGenre.MYSTERY, gold),
                BookGenreColor(BookGenre.THRILLER, gold),
        BookGenreColor(BookGenre.SCIENCE_FICTION, darkblue),
        BookGenreColor(BookGenre.FANTASY, darkblue),
        BookGenreColor(BookGenre.HISTORY, darkgreen),
        BookGenreColor(BookGenre.BIOGRAPHY, bordeaux),
        BookGenreColor(BookGenre.AUTOBIOGRAPHY, bordeaux),
        BookGenreColor(BookGenre.ROMANCE, purple),
        BookGenreColor(BookGenre.CHILDREN, orange),
        BookGenreColor(BookGenre.YOUNG_ADULT, purple),
        BookGenreColor(BookGenre.HORROR, antracyte),
        BookGenreColor(BookGenre.NON_FICTION, antracyte)
    )

    LaunchedEffect(imageUrl) {
        Log.d("BookCover", "Fetching image from URL: $imageUrl")
        try {
            image = viewModel.fetchImage(imageUrl)
        }finally {
            loadingImage = false
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        if (loadingImage) {
            CircularProgressIndicator()
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .aspectRatio(2f / 3f)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(
                    topStart = 0.dp,       // Angolo sinistro superiore ad angolo retto
                    bottomStart = 0.dp,     // Angolo sinistro inferiore ad angolo retto
                    topEnd = 8.dp,          // Angolo destro superiore leggermente arrotondato
                    bottomEnd = 8.dp        // Angolo destro inferiore leggermente arrotondato
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {

                if (image != null) {
                    Image(
                        bitmap = image!!.asImageBitmap(),
                        contentDescription = book.title,
                        modifier = Modifier
                            .fillMaxSize().padding(end = 4.dp, bottom = 8.dp),
                        contentScale = ContentScale.Inside
                    )
                } else {
                    coverColors.find { it.genre == book.genre }?.color?.let {
                        BookCoverPlaceholder(book.title, book.author,
                            it
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
}