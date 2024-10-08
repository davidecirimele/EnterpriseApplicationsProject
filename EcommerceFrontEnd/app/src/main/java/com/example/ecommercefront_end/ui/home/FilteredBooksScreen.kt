package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommercefront_end.viewmodels.BookViewModel
import com.example.ecommercefront_end.viewmodels.HomeViewModel

@Composable
fun FilteredBooksScreen(viewModel: BookViewModel, navController: NavController) {
    val products by viewModel.filteredProducts.collectAsState()
    val isLoading by viewModel.isLoadingFilteredBooks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFilteredBooks();
    }

    /*DisposableEffect(Unit) {
        onDispose {
            viewModel.resetFilter() // Ripristina il filtro
        }
    }*/

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No products matches search requirements")
            }
        } else {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Search Result",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(products.chunked(2), key = { it[0].id }) { rowBooks ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        for (book in rowBooks) {
                            ProductCard(navController, book, height = 280.dp, width = 190.dp)
                        }
                    }
                }
            }
        }
    }
}