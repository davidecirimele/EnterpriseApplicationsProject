package com.example.ecommercefront_end.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.network.RetrofitClient

import com.example.ecommercefront_end.repository.CartRepository

import com.example.ecommercefront_end.viewmodels.CartViewModel


@Composable
fun CartScreen(viewModel: CartViewModel, onCheckoutClick: () -> Unit, navController: NavController) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val totalAmount by viewModel.totalAmount.collectAsStateWithLifecycle()
    LaunchedEffect(cartItems) {
        println("Cart Items aggiornati: $cartItems")
    }



    LaunchedEffect(Unit) {
        if (SessionManager.user != null) {
            viewModel.loadCartItems()
        }
        else {
            navController.navigate("userAuth"){
                popUpTo("cart"){
                    inclusive = true
                }
            }

        }





    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (cartItems.isEmpty()) {
            Text(
                text = "Il tuo carrello è vuoto.",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItem(
                        item = item,
                        onRemoveClick = { viewModel.removeItem(item) },
                        onQuantityChange = { newQuantity: Int ->
                            viewModel.updateItemQuantity(
                                item,
                                newQuantity
                            )
                        }
                    )
                }
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