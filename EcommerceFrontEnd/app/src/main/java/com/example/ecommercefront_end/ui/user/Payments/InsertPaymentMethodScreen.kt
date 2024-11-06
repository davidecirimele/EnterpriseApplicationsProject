package com.example.ecommercefront_end.ui.user.Payments

import CheckoutViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommercefront_end.ui.checkout.CardProviderRadioGroup
import com.example.ecommercefront_end.ui.checkout.PaymentMethodTypeRadioGroup

@Composable
fun InsertPaymentMethodScreen(viewModel: CheckoutViewModel, navController: NavController){

    val cardHolder by viewModel.cardHolderName.collectAsState()
    val cardNumber by viewModel.cardNumber.collectAsState()
    val expirationDate by viewModel.expirationDate.collectAsState()
    val cardProvider by viewModel.selectedCardProvider.collectAsState()
    val selectedPaymentMethodType by viewModel.selectedPaymentMethodType.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Insert Payment Method") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    }) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(0.9f),
                verticalArrangement = Arrangement.Center,
            ) {

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text("Select Payment Method Type", style = MaterialTheme.typography.titleSmall)
                    PaymentMethodTypeRadioGroup(
                        selectedPaymentMethodType = selectedPaymentMethodType,
                        onPaymentMethodTypeSelected = { viewModel.selectPaymentMethodType(it) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    OutlinedTextField(
                        value = cardHolder,
                        onValueChange = { viewModel.onCardHolderNameChange(it) },
                        label = { Text("Holder") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { viewModel.onCardNumberChange(it) },
                        label = { Text("Card Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Text("Select Card Provider", style = MaterialTheme.typography.titleSmall)
                    CardProviderRadioGroup(
                        selectedProvider = cardProvider,
                        onProviderSelected = { viewModel.selectCardProvider(it) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    TextField(
                        value = expirationDate,
                        onValueChange = { viewModel.onExpirationDateChange(it) },
                        label = { Text("Expiration Date (MM/YY)") }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Button(
                onClick = {
                    viewModel.onAddPaymentMethodClick()

                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = cardHolder.isNotBlank() && cardProvider != null && cardNumber.isNotBlank() && expirationDate.isNotBlank() && selectedPaymentMethodType != null
            ) {
                Text("Save", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}