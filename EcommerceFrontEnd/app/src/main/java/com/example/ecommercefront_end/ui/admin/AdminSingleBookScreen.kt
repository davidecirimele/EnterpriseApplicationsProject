package com.example.ecommercefront_end.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.ui.home.testImgs
import com.example.ecommercefront_end.viewmodels.BookViewModel
import java.time.format.DateTimeFormatter

@Composable
fun AdminSingleBookScreen(book: Book, bookViewModel: BookViewModel) {

    var showEditPriceField by remember { mutableStateOf(false) }
    var showRestockField by remember { mutableStateOf(false) }

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
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = book.author,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )
            }
        }

        item {
            val imageUrl = remember(book.id) {
                testImgs[book.id.hashCode() % testImgs.size]
            }
            val imagePainter = rememberAsyncImagePainter(
                model = imageUrl,
                error = rememberAsyncImagePainter("https://mockuptree.com/wp-content/uploads/edd/2019/10/free-Book-mockup-150x150.jpg")
            )
            Image(
                painter = imagePainter,
                contentDescription = book.title,
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
                if(!showRestockField)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text(
                            text = "Price: € ${"%,.2f".format(book.price)}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row {
                        Text(text = "Edit Price",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically).clickable { showEditPriceField = true })
                    }
                    if(showEditPriceField){
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row {
                            TextFieldWithSubmitButton({ newPrice ->
                                bookViewModel.updatePrice(newPrice.toDouble(), bookId = book.id);
                                showEditPriceField = false;
                            }, book.id)
                        }
                    }
                }
                if(!showEditPriceField)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            Text("Stock Quantity: ${book.stock}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterVertically))
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row {
                            Text("Restock",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterVertically).clickable { showRestockField = true })
                        }
                        if(showRestockField){
                            Spacer(modifier = Modifier.padding(8.dp))
                            Row{
                            TextFieldWithSubmitButton({
                                newStock -> bookViewModel.restock(newStock.toInt(), bookId = book.id);
                                showRestockField = false },book.id)
                                }
                        }
                    }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = {/*TODO*/},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 18.dp)
                ) {
                    Text("Remove from catalogue")
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }

        item {
            Text(
                text = "Product Details",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .background(Color.LightGray.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "Author",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra
                        )
                        Text(
                            text = book.author,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "ISBN",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = book.ISBN,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Number of pages",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = "${book.pages}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Edition",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = book.edition,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Format",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra
                        )
                        Text(
                            text = book.format?.name ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Genre",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = book.genre?.name ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Language",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = book.language?.name ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Publisher",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = book.publisher,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Age",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = if (book.age != null) {
                                "${book.age}"
                            }else {
                                "--" // O un altro messaggio di default
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Publish Date",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            text = if (book.publishDate != null) {
                                book.publishDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            } else {
                                "--" // O un altro messaggio di default
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Weight",
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.LightGray.copy(alpha = 0.2f)) // Ombreggia la colonna di destra

                        )
                        Text(
                            if (book.weight != null) {
                                "${book.weight} kg"
                            } else {
                                " --" // O un altro messaggio di default
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

        }
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