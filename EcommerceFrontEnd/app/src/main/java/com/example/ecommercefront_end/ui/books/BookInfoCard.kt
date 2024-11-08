package com.example.ecommercefront_end.ui.books

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommercefront_end.model.Book
import java.time.format.DateTimeFormatter

@Composable
fun BookInfoCard(book: Book){
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
                        .background(Color.LightGray.copy(alpha = 0.2f))
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
                        .background(Color.LightGray.copy(alpha = 0.2f))

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
                        .background(Color.LightGray.copy(alpha = 0.2f))

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
                        .background(Color.LightGray.copy(alpha = 0.2f))

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
                        .background(Color.LightGray.copy(alpha = 0.2f))
                )
                Text(
                    text = book.format.name ?: "",
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
                        .background(Color.LightGray.copy(alpha = 0.2f))

                )
                Text(
                    text = book.genre.name ?: "",
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
                        .background(Color.LightGray.copy(alpha = 0.2f))

                )
                Text(
                    text = book.language.name ?: "",
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
                        .background(Color.LightGray.copy(alpha = 0.2f))

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
                    } else {
                        "--"
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
                    text = if (book!!.publishDate != null) {
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
                        " --"
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}