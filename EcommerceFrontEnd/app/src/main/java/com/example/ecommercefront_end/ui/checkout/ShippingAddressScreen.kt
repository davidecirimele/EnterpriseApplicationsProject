package com.example.ecommercefront_end.ui.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommercefront_end.viewmodels.CheckoutViewModel


@Composable
fun ShippingAddressScreen(
    viewModel: CheckoutViewModel,
    onNavigateToPayment: () -> Unit
) {
    val address by viewModel.shippingAddress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Indirizzo di Spedizione",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = address.name,
            onValueChange = { viewModel.updateShippingAddress(address.copy(name = it)) },
            label = { Text("Nome e Cognome") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = address.street,
            onValueChange = { viewModel.updateShippingAddress(address.copy(street = it)) },
            label = { Text("Indirizzo") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = address.city,
                onValueChange = { viewModel.updateShippingAddress(address.copy(city = it)) },
                label = { Text("Città") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = address.postalCode,
                onValueChange = { viewModel.updateShippingAddress(address.copy(postalCode = it)) },
                label = { Text("CAP") },
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = address.country,
            onValueChange = { viewModel.updateShippingAddress(address.copy(country = it)) },
            label = { Text("Paese") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onNavigateToPayment,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Procedi al Pagamento")
        }
    }
}