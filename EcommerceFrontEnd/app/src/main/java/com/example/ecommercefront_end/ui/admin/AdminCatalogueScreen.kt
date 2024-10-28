package com.example.ecommercefront_end.ui.admin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.ui.books.BooksFilterScreen
import com.example.ecommercefront_end.ui.home.testImgs
import com.example.ecommercefront_end.viewmodels.BookViewModel
import okhttp3.OkHttpClient

fun Modifier.bookEntryModifier(navController: NavController, bookId: Long) = composed {
    this
        .fillMaxWidth()
        .height(200.dp)
        .clickable { navController.navigate("/admin/book_details/${bookId}") }
}
@Composable
fun AdminCatalogueScreen(bookViewModel: BookViewModel, navHostController: NavHostController){

    val products by bookViewModel.filteredProducts.collectAsState()

    var searchValue by remember { mutableStateOf("") }

    var filterOptions by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        bookViewModel.fetchAllAvailableProducts()
    }

    Column(modifier = Modifier.fillMaxSize()){

        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(
                text = "Catalogue",
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchValue,
                onValueChange = { searchValue = it;
                    bookViewModel.updateFilter(title = it, author = it, publisher = it);
                    bookViewModel.searchBooks(navController = navHostController,"admin-catalogue")
                },
                label = { Text("Search by Title, Author, Publisher or ISBN") },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
            )
            IconButton(modifier = Modifier.weight(1f).align(Alignment.CenterVertically),onClick = {
                filterOptions = !filterOptions
            }) {
                Icon(
                    Icons.Filled.FilterAlt,
                    contentDescription = "Filter Catalogue",
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            if(products.isNotEmpty()) {
                for ((index,product) in products.withIndex())
                    item {
                        Row(){
                            Text(text = (index + 1).toString(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(4.dp))
                            Spacer(modifier = Modifier.padding(2.dp))
                            BookEntry(book = product, bookViewModel, navHostController)
                        }
                    }
            }
            else{
                item{
                    Text(text = "No books")
                }
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            addNewProductCard(navHostController = navHostController)
        }
    }

    if(filterOptions) {
        BooksFilterScreen(
            viewModel = bookViewModel,
            navController = navHostController,
            currentRoute = "admin-catalogue",
            onDismiss = {
                filterOptions = false
            })
    }
}

@Composable
fun BookEntry(book: Book, bookViewModel: BookViewModel, navHostController: NavHostController){

    Row(modifier = Modifier.bookEntryModifier(navHostController,book.id)) {
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
                        BookCover(book, bookViewModel)
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
fun addNewProductCard(navHostController: NavHostController){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            navHostController.navigate("insert-product") {
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