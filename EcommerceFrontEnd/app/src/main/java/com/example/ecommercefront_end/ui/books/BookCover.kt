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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
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
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.viewmodels.BookViewModel


@Composable
fun BookCover(book: Book, viewModel: BookViewModel) {
    Log.d("BookCover", "Fetching image for : $book")
    var loadingImage by remember { mutableStateOf(true) }
    val imageUrl = book.imagePath

    var image by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUrl) {
        Log.d("BookCover", "Fetching image from URL: $imageUrl")
        try {
            if(book.imagePath == null) {
                Log.d("BookCover", "Cover URL of ${book.title} is null : $imageUrl")
            }
            image = viewModel.fetchImage(imageUrl)
        }finally {
            loadingImage = false
        }
    }

    if (loadingImage) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            CircularProgressIndicator()
        }
    } else {
        if (image != null) {
            Image(
                bitmap = image!!.asImageBitmap(),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Inside
            )
        }
        else{
            Text("Cover not found")
        }
    }
}