package com.example.ecommercefront_end.ui.checkout

import androidx.compose.foundation.layout.*

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommercefront_end.viewmodels.CheckoutViewModel


@Composable
fun ShippingAddressScreen(
    viewModel: CheckoutViewModel,
    onNavigateToPayment: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var address by remember { mutableStateOf(uiState.shippingAddress) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Indirizzo di Spedizione",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = address.name,
            onValueChange = { address = address.copy(name = it) },
            label = { Text("Nome e Cognome") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = address.street,
            onValueChange = { address = address.copy(street = it) },
            label = { Text("Indirizzo") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = address.city,
                onValueChange = { address = address.copy(city = it) },
                label = { Text("Città") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            OutlinedTextField(
                value = address.postalCode,
                onValueChange = { address = address.copy(postalCode = it) },
                label = { Text("CAP") },
                modifier = Modifier.width(120.dp)
            )
        }

        OutlinedTextField(
            value = address.country,
            onValueChange = { address = address.copy(country = it) },
            label = { Text("Paese") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.updateShippingAddress(address)
                onNavigateToPayment()
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            enabled = address.isValid() // Assumiamo che ShippingAddress abbia un metodo isValid()
        ) {
            Text("Procedi al Pagamento")
        }
    }
}