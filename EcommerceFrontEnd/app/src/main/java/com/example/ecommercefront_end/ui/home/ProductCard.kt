package com.example.ecommercefront_end.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.viewmodels.BookViewModel


fun Modifier.productCardModifier(navController: NavController, bookId: Long) = composed {
    this
        .fillMaxSize()
        .aspectRatio(2f / 3f)
        .clickable { navController.navigate("/books_details/${bookId}") }
}

@Composable
fun ProductCard(navController: NavController, book: Book, bookViewModel: BookViewModel) {
    var cardWidth by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .productCardModifier(bookId = book.id, navController = navController)
            .onGloballyPositioned { layoutCoordinates ->
                cardWidth = layoutCoordinates.size.width
            }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BookCover(
                book = book,
                viewModel = bookViewModel,
                navController
            )

            Spacer(modifier = Modifier.height(2.dp))

            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = (cardWidth / 35).sp,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )

                Text(
                    text = book.author,
                    fontSize = (cardWidth / 40).sp,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "€ ${"%,.2f".format(book.price)}",
                    fontSize = (cardWidth / 35).sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}