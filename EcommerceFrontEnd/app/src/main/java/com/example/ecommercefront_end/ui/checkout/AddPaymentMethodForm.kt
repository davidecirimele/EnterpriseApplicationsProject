package com.example.ecommercefront_end.ui.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommercefront_end.model.CardProvider
import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.PaymentMethodType

@Composable
fun AddPaymentMethodForm(
    isExpanded: Boolean,
    onExpandChange: () -> Unit,
    cardHolderName: String,
    cardNumber: String,
    expirationDate: String,
    selectedCardProvider: CardProvider?,
    selectedPaymentMethodType: PaymentMethodType?,
    onCardHolderNameChange: (String) -> Unit,
    onCardNumberChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onCardProviderSelected:   (CardProvider?) -> Unit,
    onPaymentMethodTypeSelected: (PaymentMethodType) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // RIGA CHE MOSTRA L'INTESTAZIONE DEL FORM E LA FRECCIA PER ESPANDERE/COMPRIMERE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandChange() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Add New Payment Method", style = MaterialTheme.typography.headlineSmall)
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }

        // FORM PER L'AGGIUNTA DI UN NUOVO METODO DI PAGAMENTO
        if (isExpanded) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Payment Method Type", style = MaterialTheme.typography.titleSmall)
            PaymentMethodTypeRadioGroup(
                selectedPaymentMethodType = selectedPaymentMethodType,
                onPaymentMethodTypeSelected = onPaymentMethodTypeSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo per il titolare della carta
            TextField(
                value = cardHolderName,
                onValueChange = onCardHolderNameChange,
                label = { Text("Card Holder Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo per il numero della carta
            TextField(
                value = cardNumber,
                onValueChange = onCardNumberChange,
                label = { Text("Card Number") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Card Provider", style = MaterialTheme.typography.titleSmall)
            CardProviderRadioGroup(
                selectedProvider = selectedCardProvider,
                onProviderSelected = onCardProviderSelected
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo per la data di scadenza
            TextField(
                value = expirationDate,
                onValueChange = onExpirationDateChange,
                label = { Text("Expiration Date (MM/YY)") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pulsante per aggiungere il nuovo metodo di pagamento
            Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) {
                Text("Add Payment Method")
            }
        }
    }
}

@Composable
fun PaymentMethodTypeRadioGroup(
    selectedPaymentMethodType: PaymentMethodType?,
    onPaymentMethodTypeSelected: (PaymentMethodType) -> Unit
) {
    Column {
        PaymentMethodType.entries.forEach { type ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPaymentMethodTypeSelected(type) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = type == selectedPaymentMethodType,
                    onClick = { onPaymentMethodTypeSelected(type) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = type.displayName)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
