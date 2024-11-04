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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.volley.toolbox.ImageRequest
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.ui.admin.AdminCatalogueScreen
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.ui.books.BooksFilterScreen
import com.example.ecommercefront_end.utils.SearchBar
import com.example.ecommercefront_end.viewmodels.BookViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(bookViewModel: BookViewModel, navController: NavController) {

    val allProducts by bookViewModel.allAvailableProducts.collectAsState()

    var filteredProducts by remember { mutableStateOf(emptyList<Book>()) }

    var filterOptions by remember { mutableStateOf(false) }

    var searchValue by remember { mutableStateOf("") }

    val colorScheme = androidx.compose.material3.MaterialTheme.colorScheme

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        if (allProducts.isEmpty())
            bookViewModel.fetchAllAvailableProducts()
    }

    LaunchedEffect(searchValue) {
        filteredProducts = if (searchValue != "")
            bookViewModel.localFilter(allProducts, BookFilter(title = searchValue, author = searchValue, publisher = searchValue))
        else
            emptyList()
    }

    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = colorScheme.primary,
            contentColor = colorScheme.secondary,
            modifier = Modifier.height(70.dp)
        ){
            SearchBar(filterOptions,{ newValue -> filterOptions = newValue } , {value -> searchValue = value })
        }
    }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)){

            if (searchValue == "" && filteredProducts.isEmpty()) {
                CatalogueScreen(allProducts, bookViewModel, navController)
            }
            else{
                FilteredBooksScreen(filteredProducts, onDismissOrderOptions = { bookViewModel.sortProducts(filteredProducts) } ,bookViewModel, navController)
            }

            if(filterOptions) {
                BooksFilterScreen(
                    viewModel = bookViewModel,
                    onSearchBooks = { filter ->
                        run {
                            coroutineScope.launch {
                                filteredProducts = bookViewModel.fetchBooksByFilter(filter)
                            }
                        }
                    },
                    currentRoute = "home",
                    onDismiss = {
                        filterOptions = false
                    })
            }
        }
    }

}


@Composable
fun FilteredBooksScreen(products: List<Book>, onDismissOrderOptions: ()->Unit, bookViewModel: BookViewModel, navController: NavController) {

    val isLoading by bookViewModel.isLoadingFilteredBooks.collectAsState()

    val error by bookViewModel.error.collectAsState()

    var orderOptions by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(products) {
        listState.scrollToItem(0)
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
            val sortedProducts = bookViewModel.sortProducts(products);
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text(
                        text = "Search Results",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        onClick = {
                            if (!orderOptions)
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
                BooksGridView("Results", sortedProducts, navController, bookViewModel)

                if (orderOptions) {
                    OrderOptionsScreen(bookViewModel, onDismiss = {
                        orderOptions = false;
                        onDismissOrderOptions()
                    })
                }
            }
        }

    }
}

@Composable
fun CatalogueScreen(products: List<Book>, bookViewModel: BookViewModel, navController: NavController){

    val isLoading by bookViewModel.isLoadingCatalogue.collectAsState()
    val error by bookViewModel.error.collectAsState()

    val halloweenSelection by bookViewModel.halloweenBooks.collectAsState()
    val recentsSelection by bookViewModel.recentBooks.collectAsState()



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
                        ProductSectionView(
                            navController,
                            title = "Spooky Halloween",
                            books = halloweenSelection,
                            bookViewModel = bookViewModel
                        )
                    }

                    item {

                        ProductSectionView(
                            navController,
                            title = "From 2020 to the present",
                            books = recentsSelection,
                            bookViewModel = bookViewModel
                        )
                    }

                    item {
                        Box(modifier = Modifier.heightIn(min = 200.dp, max = 1000.dp)) {
                            BooksGridView(
                                title = "Catalogue",
                                products,
                                navController,
                                bookViewModel
                            )
                        }
                    }
                }
            }
        }
}






