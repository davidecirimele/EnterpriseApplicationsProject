package com.example.ecommercefront_end.ui.cart

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommercefront_end.viewmodels.CartViewModel
import com.example.ecommercefront_end.viewmodels.CheckoutViewModel

@Composable
fun CartScreen(viewModel: CartViewModel, onCheckoutClick: () -> Unit) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems) { item ->
                CartItem(
                    item = item,
                    onRemoveClick = { viewModel.removeItem(item) },
                    onQuantityChange = { newQuantity: Int -> viewModel.updateItemQuantity(item, newQuantity) }
                )
            }
        }

        TotalSection(totalAmount = totalAmount, onCheckoutClick = onCheckoutClick)
    }
}



@Composable
fun TotalSection(totalAmount: Double, onCheckoutClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total ", style = MaterialTheme.typography.titleMedium)
            Text("$totalAmount €", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onCheckoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Checkout")
        }
    }
}