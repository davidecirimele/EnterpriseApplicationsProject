package com.example.ecommercefront_end.ui.admin

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.ui.home.testImgs
import com.example.ecommercefront_end.viewmodels.BookViewModel

fun Modifier.bookEntryModifier(navController: NavController, bookId: Long) = composed {
    this
        .fillMaxWidth()
        .height(200.dp)
        .clickable { navController.navigate("/books_details/${bookId}") }
}
@Composable
fun AdminHomeScreen(bookViewModel: BookViewModel, navHostController: NavHostController){

    val products by bookViewModel.allProducts.collectAsState()

    Column(modifier = Modifier.fillMaxSize()){
        Column() {
            Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(
                    text = "All products",
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            addNewProductCard(navHostController = navHostController)
            Spacer(modifier = Modifier.padding(8.dp))
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
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
                            BookEntry(book = product, navHostController)
                        }
                    }
            }
            else{
                item{
                    Text(text = "No books")
                }
            }
        }
    }
}

@Composable
fun BookEntry(book: Book, navHostController: NavHostController){

    val imageUrl = remember(book.id) {
        testImgs[book.id.hashCode() % testImgs.size]
    }

    val imagePainter = rememberAsyncImagePainter(
        model = imageUrl,
        error = rememberAsyncImagePainter("https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg")
    )
    val isLoading = imagePainter.state is AsyncImagePainter.State.Loading

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
                        Image(
                            painter = imagePainter,
                            contentDescription = book.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
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