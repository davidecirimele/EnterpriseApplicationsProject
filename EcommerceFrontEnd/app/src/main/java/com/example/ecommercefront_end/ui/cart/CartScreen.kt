package com.example.ecommercefront_end.ui.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberScaffoldState

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult


import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.network.RetrofitClient

import com.example.ecommercefront_end.repository.CartRepository

import com.example.ecommercefront_end.viewmodels.CartViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartScreen(viewModel: CartViewModel, onCheckoutClick: () -> Unit, navController: NavController) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val totalAmount by viewModel.totalAmount.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isCheckoutEnabled by viewModel.isCheckoutEnabled.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            viewModel.loadCartItems()
        }
    )

    /*LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            val result = viewModel.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "RIPROVA"
            )

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.loadCartItems() // Riprova a caricare gli articoli
            }

            viewModel.clearErrorMessage() // Reset del messaggio d'errore
        }
    }*/

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { message ->
            snackbarHostState.showSnackbar(message)

        }
    }





        LaunchedEffect(Unit) {
            if (SessionManager.user != null) {
                viewModel.loadCartItems()
            } else {
                navController.navigate("userAuth") {
                    popUpTo("cart") {
                        inclusive = true
                    }
                }

            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)  // Distribuisci lo spazio tra la parte superiore e la parte inferiore
                        .fillMaxWidth()
                        .pullRefresh(refreshState)
                        .padding(16.dp)
                ) {
                    if (isLoading) {

                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        if (cartItems.isEmpty()) {

                            Text(
                                text = "Your cart is empty.",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {

                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(cartItems) { item ->
                                    CartItem(
                                        item = item,
                                        onRemoveClick = { viewModel.removeItem(item) },
                                        onQuantityChange = { newQuantity ->
                                            viewModel.updateItemQuantity(item, newQuantity)
                                        }
                                    )
                                }
                            }
                        }
                    }


                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = refreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }


                TotalSection(
                    totalAmount = totalAmount,
                    onCheckoutClick = onCheckoutClick,
                    isCheckoutEnabled = isCheckoutEnabled
                )
            }
        }
    }



    @SuppressLint("DefaultLocale")
    @Composable
    fun TotalSection(totalAmount: Double, onCheckoutClick: () -> Unit, isCheckoutEnabled: Boolean) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total ", style = MaterialTheme.typography.titleMedium)
                Text(String.format("%.2f €", totalAmount), style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCheckoutClick,
                modifier = Modifier.fillMaxWidth().background(
                    if (isCheckoutEnabled) MaterialTheme.colorScheme.primary else Color.Gray,
                ),
                enabled = isCheckoutEnabled
            ) {
                Text("Checkout")
            }
        }
    }
