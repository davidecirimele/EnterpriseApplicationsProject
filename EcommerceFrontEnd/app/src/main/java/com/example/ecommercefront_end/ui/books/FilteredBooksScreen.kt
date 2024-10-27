package com.example.ecommercefront_end.ui.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommercefront_end.ui.home.OrderOptionsScreen
import com.example.ecommercefront_end.ui.home.ProductCard
import com.example.ecommercefront_end.viewmodels.BookViewModel
import kotlinx.coroutines.launch

@Composable
fun FilteredBooksScreen(bookViewModel: BookViewModel,navController: NavController) {
    val products by bookViewModel.filteredProducts.collectAsState()
    val isLoading by bookViewModel.isLoadingFilteredBooks.collectAsState()
    var orderOptions by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(products) {
        coroutineScope.launch {
            listState.scrollToItem(0)
        }
    }

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
            bookViewModel.sortProducts();
            Column(modifier = Modifier.fillMaxSize()){
                Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text(
                        text = "Search Results",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(modifier = Modifier.align(Alignment.CenterVertically),onClick = {
                        if(!orderOptions)
                            orderOptions = true
                        else
                            orderOptions = false
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort Books",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
                LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                    items(products.chunked(2), key = { it[0].id }) { rowBooks ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            for (book in rowBooks) {
                                ProductCard(navController, book, height = 230.dp, width = 170.dp)
                            }
                        }
                    }
                }

                if(orderOptions){
                    OrderOptionsScreen(bookViewModel, onDismiss = {orderOptions = false;
                        bookViewModel.sortProducts()
                        coroutineScope.launch {
                            listState.scrollToItem(0)
                        }
                    })
                }
            }
        }
    }
}