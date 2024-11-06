package com.example.ecommercefront_end.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommercefront_end.model.OrderSummary
import com.example.ecommercefront_end.viewmodels.AdminViewModel

@Composable
fun AdminOrdersScreen(
    viewModel: AdminViewModel,
    onOrderClick: (Long) -> Unit // Lambda per gestire il click su un ordine e navigare al dettaglio
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        if (orders.isEmpty())
        viewModel.fetchOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Management") },
                backgroundColor = Color(0xFF1F1F1F),
                contentColor = Color.White
            )
        }
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
                    items(orders) { order ->
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
                    Button(
                        onClick = { viewModel.loadPreviousPage() },
                        enabled = viewModel.currentPage > 0
                    ) {
                        Text("Previous")
                    }
                    Button(
                        onClick = { viewModel.loadNextPage() },
                        enabled = viewModel.currentPage + 1 < viewModel.totalPages
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: OrderSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        backgroundColor = Color.DarkGray,
        contentColor = Color.White,
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Order ID: ${order.orderId}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "User: ${order.email}")
            Text(text = "Order date: ${order.orderDate}")
            Text(text = "Total amount: €${order.totalAmount}")
            Text(text = "Status: ${order.orderStatus}")
        }
    }
}
