package com.example.ecommercefront_end.ui.cart

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.asImageBitmap

import com.example.ecommercefront_end.model.CartItem
import com.example.ecommercefront_end.model.OrderItem
import java.io.File


@Composable
fun CartItem(
    item: CartItem,
    onRemoveClick: () -> Unit,
    onQuantityChange: (Int) -> Unit,
) {


    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        // Immagine del prodotto
        //FIXME : add image url

        val bitmap = BitmapFactory.decodeByteArray(item.bookId.cover, 0, item.bookId.cover.size)
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Product image",
            modifier = Modifier.size(100.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )


        // Dettagli del prodotto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.bookId.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Di: ${item.bookId.author}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Prezzo: ${item.bookId.price}€",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))


            // Controlli per la quantità
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (item.quantity > 1) {
                        item.quantity -= 1
                        onQuantityChange(item.quantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
                }
                Text(text = "${item.quantity}", style = MaterialTheme.typography.bodyLarge)
                IconButton(onClick = {
                    item.quantity += 1
                    onQuantityChange(item.quantity)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase quantity")
                }
            }
        }

        // Pulsante per rimuovere l'articolo
        IconButton(onClick = onRemoveClick) {
            Icon(Icons.Default.Delete, contentDescription = "Remove item", tint = Color.Red)
        }
    }
}