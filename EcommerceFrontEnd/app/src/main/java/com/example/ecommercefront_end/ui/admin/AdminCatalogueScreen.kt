package com.example.ecommercefront_end.ui.admin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDefaults.contentColor
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.ui.books.BooksFilterScreen
import com.example.ecommercefront_end.utils.SearchBar
import com.example.ecommercefront_end.viewmodels.BookViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

fun Modifier.bookEntryModifier(navController: NavController, bookId: Long) = composed {
    this
        .fillMaxWidth()
        .fillMaxHeight(0.35f)
        .clickable { navController.navigate("/admin/book_details/${bookId}") }
}
@Composable
fun AdminCatalogueScreen(bookViewModel: BookViewModel, navController: NavController){

    val allProducts by bookViewModel.allAvailableProducts.collectAsState()

    var filteredProducts by remember { mutableStateOf(emptyList<Book>()) }

    var searchValue by remember { mutableStateOf("") }

    var filterOptions by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val errorMessage by bookViewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {

        errorMessage?.let { message ->
            val result = bookViewModel.snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
                withDismissAction = false,
                )

            if (result == SnackbarResult.Dismissed) {
                bookViewModel.onSnackbarDismissed()
            }
        }

    }

    LaunchedEffect(searchValue) {
        if(searchValue == "")
            filteredProducts = allProducts
        else {
            val filter =
                BookFilter(title = searchValue, author = searchValue, publisher = searchValue)
            filteredProducts = bookViewModel.localFilter(allProducts, filter)
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = colorScheme.primary,
            contentColor = colorScheme.secondary,
            modifier = Modifier.height(70.dp)
        ){
            SearchBar(filterOptions,{ newValue -> filterOptions = newValue } , {value -> searchValue = value })
        }

    },snackbarHost = { SnackbarHost(bookViewModel.snackbarHostState) }){ paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

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
                    currentRoute = "admin-catalogue",
                    onDismiss = {
                        filterOptions = false
                    })
            }

            Spacer(modifier = Modifier.padding(2.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (filteredProducts.isNotEmpty()) {
                    for ((index, product) in filteredProducts.withIndex())
                        item {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = (index + 1).toString(),
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(2.dp)
                                        .fillMaxWidth(0.08f),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.padding(2.dp))
                                BookEntry(book = product, bookViewModel, navController)
                            }
                        }
                } else {
                    item {
                        Text(text = "No books")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                addNewProductCard(navController = navController)
            }
        }
    }
}

@Composable
fun BookEntry(book: Book, bookViewModel: BookViewModel, navController: NavController){

    Row(modifier = Modifier.bookEntryModifier(navController,book.id)) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)) {
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .height((250 * 0.6f).dp)
                            .width((200 * 0.6f).dp)
                    ) {
                        BookCover(book, bookViewModel, navController)
                    }
                    Spacer(modifier = Modifier.padding(4.dp))

                    Column {
                        Row {
                            Text("Title", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(text = book.title,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        }
                        Row {
                            Text("Author", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(text = book.author,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        }
                        Row {
                            Text("Publisher", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(text = book.publisher,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        }
                        Row {
                            Text("Price", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(text = book.price.toString())
                        }

                        Row{
                            Text("Stock", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(text = book.stock.toString())
                        }
                    }
                }
                Row {
                    Row {
                        Text("ISBN", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(text = book.ISBN,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
fun addNewProductCard(navController: NavController){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            navController.navigate("insert-product") {
                popUpTo("admin-home") {
                    saveState = true
                }
            }
        })) {
        Icon(
            Icons.Filled.AddBox,
            contentDescription = "Insert Product",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}