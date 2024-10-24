package com.example.ecommercefront_end.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.ui.books.BookInfoCard
import com.example.ecommercefront_end.ui.home.testImgs
import com.example.ecommercefront_end.viewmodels.BookViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun AdminSingleBookScreen( bookViewModel: BookViewModel, navHostController: NavHostController) {

    val book by bookViewModel.bookFlow.collectAsState();
    var showEditPriceField by remember { mutableStateOf(false) }
    var showRestockField by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val isLoading by bookViewModel.isLoadingBook.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = book!!.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                    Text(
                        text = book!!.author,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )
                }
            }

            item {
                val imageUrl = remember(book!!.id) {
                    testImgs[book!!.id.hashCode() % testImgs.size]
                }
                val imagePainter = rememberAsyncImagePainter(
                    model = imageUrl,
                    error = rememberAsyncImagePainter("https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg")
                )
                Image(
                    painter = imagePainter,
                    contentDescription = book!!.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }


            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!showRestockField) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row {
                                Text(
                                    text = "Price: € ${"%,.2f".format(book!!.price)}",
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            Spacer(modifier = Modifier.padding(8.dp))
                            Row {
                                var isClicked by remember { mutableStateOf(false) }
                                Text(text = "Edit Price",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isClicked) Color.Blue else Color.Black,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                        .clickable {
                                            isClicked = !isClicked
                                            showEditPriceField = true
                                        }.padding(16.dp)
                                )
                            }
                            if (showEditPriceField) {
                                Spacer(modifier = Modifier.padding(8.dp))
                                Row {
                                    TextFieldWithSubmitButton({ newPrice ->
                                        bookViewModel.updatePrice(
                                            newPrice.toDouble(),
                                            bookId = book!!.id
                                        );
                                        coroutineScope.launch {
                                            bookViewModel.fetchAllAvailableProducts()
                                            bookViewModel.loadBook(book!!.id)
                                        }
                                        showEditPriceField = false;
                                    }, book!!.id)
                                }
                            }
                        }
                    }
                    if (!showEditPriceField) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row {
                                Text(
                                    "Stock Quantity: ${book!!.stock}",
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                            Spacer(modifier = Modifier.padding(8.dp))
                            Row {
                                var isClicked by remember { mutableStateOf(false) }
                                Text("Restock",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isClicked) Color.Blue else Color.Black,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                        .clickable {
                                            isClicked = !isClicked;
                                            showRestockField = true
                                        }.padding(16.dp)
                                )
                            }
                            if (showRestockField) {
                                Spacer(modifier = Modifier.padding(8.dp))
                                Row {
                                    TextFieldWithSubmitButton({ newStock ->
                                        bookViewModel.restock(newStock.toInt(), bookId = book!!.id);
                                        coroutineScope.launch {
                                            bookViewModel.fetchAllAvailableProducts()
                                            bookViewModel.loadBook(book!!.id)
                                        }
                                        showRestockField = false
                                    }, book!!.id)
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            if(book!!.available) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(bottom = 18.dp)
                        ) {
                            Text("Remove from catalogue")
                        }
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(bottom = 18.dp)
                        ) {
                            Text("Add to catalogue")
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            item {
                BookInfoCard(book!!)
            }
        }
    }

    if (showDialog) {
        val showedText = if (book?.available == true) {
            "Remove"
        } else {
            "Add"
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm $showedText") },
            text = { Text("Are you sure you want to $showedText this product?") },
            confirmButton = {
                Button(
                    onClick = {
                        if(book?.available == true)
                            book?.let { bookViewModel.removeBook(it.id) }
                        else
                            book?.let { bookViewModel.restoreBook(it.id) }

                        navHostController.popBackStack()
                        showDialog = false

                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TextFieldWithSubmitButton(
    onSubmit: (String) -> Unit, bookId: Long
) {
    var text by remember { mutableStateOf("") }

    Row{
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSubmit(text)
                }
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = {
            onSubmit(text)
        }) {
            Text("Submit")
        }
    }
}