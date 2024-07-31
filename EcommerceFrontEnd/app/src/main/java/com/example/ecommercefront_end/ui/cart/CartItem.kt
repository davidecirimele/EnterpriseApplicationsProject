package com.example.ecommercefront_end.ui.cart

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
import coil.compose.rememberAsyncImagePainter
import com.example.ecommercefront_end.model.OrderItem



@Composable
fun CartItem(
    item: OrderItem,
    onRemoveClick: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    // Gestione dello stato locale per la quantità
    var quantity by remember { mutableStateOf(item.quantity) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        // Immagine del prodotto
        Image(
            painter = rememberAsyncImagePainter(model = item.product.imageUrl),
            contentDescription = item.product.name,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 16.dp)
        )

        // Dettagli del prodotto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Price: ${item.product.price} €",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Color: ${item.product.color}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Size: ${item.product.size}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Controlli per la quantità
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (quantity > 1) {
                        quantity -= 1
                        onQuantityChange(quantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
                }
                Text(text = "$quantity", style = MaterialTheme.typography.bodyLarge)
                IconButton(onClick = {
                    quantity += 1
                    onQuantityChange(quantity)
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

