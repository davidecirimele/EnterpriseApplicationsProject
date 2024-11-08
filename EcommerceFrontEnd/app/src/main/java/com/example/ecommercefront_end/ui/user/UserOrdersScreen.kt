package com.example.ecommercefront_end.ui.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecommercefront_end.model.Order
import com.example.ecommercefront_end.model.OrderSummary
import com.example.ecommercefront_end.ui.admin.OrderItem
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AdminViewModel

@Composable
fun UserOrdersScreen(
    viewModel: AccountViewModel,
    onOrderClick: (Long) -> Unit // Lambda per gestire il click su un ordine e navigare al dettaglio
) {
    val orders by viewModel.userOrders.collectAsState()
    val isLoading by viewModel.isLoadingOrders.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            val result = viewModel.snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
                withDismissAction = false,
            )

            if (result == SnackbarResult.Dismissed) {
                viewModel.onSnackbarDismissed()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Management") },
                backgroundColor = Color(0xFF1F1F1F),
                contentColor = Color.White
            )
        },snackbarHost = { SnackbarHost(viewModel.snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(orders ?: emptyList()) { order ->
                        OrderItem(order) {
                            onOrderClick(order.orderId)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }
            }
        }
    }
}