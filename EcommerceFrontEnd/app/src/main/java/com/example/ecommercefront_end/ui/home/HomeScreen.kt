package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.volley.toolbox.ImageRequest
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.viewmodels.BookViewModel
import java.time.LocalDate

@Composable
fun HomeScreen(bookViewModel: BookViewModel, navController: NavController) {
    val products by bookViewModel.allAvailableProducts.collectAsState()
    val isLoading by bookViewModel.isLoadingCatalogue.collectAsState()
    val error by bookViewModel.error.collectAsState()

    val recentsSelection = remember { mutableStateOf(emptyList<Book>()) }
    val halloweenSelection = remember { mutableStateOf(emptyList<Book>()) }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error")
        }
    } else {
        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No Available Products")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {


                item {

                    LaunchedEffect(Unit) {
                        halloweenSelection.value = bookViewModel.fetchBooksByFilter(BookFilter(genre = BookGenre.HORROR))
                    }
                    ProductSectionView(
                        navController,
                        title = "Spooky Halloween",
                        books = halloweenSelection.value,
                        bookViewModel = bookViewModel
                    )
                }

                item {

                    LaunchedEffect(Unit) {
                        recentsSelection.value = bookViewModel.fetchBooksByFilter(BookFilter(maxPublishDate = LocalDate.now(), minPublishDate = LocalDate.now().minusYears(4)))
                    }
                    ProductSectionView(
                        navController,
                        title = "From 2020 to the present",
                        books = recentsSelection.value,
                        bookViewModel = bookViewModel
                    )
                }

                item {
                    Box(modifier = Modifier.heightIn(min = 200.dp, max = 1000.dp)) {
                        BooksGridView(title = "Catalogue",products, navController, bookViewModel)
                    }
                }
            }
        }
    }
}





