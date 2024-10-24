package com.example.ecommercefront_end.ui.checkout

import CheckoutViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommercefront_end.model.PaymentMethod

@Composable
fun CheckoutPaymentScreen(viewModel: CheckoutViewModel, navController: NavController) {
    val paymentMethods by viewModel.paymentMethods.collectAsState()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsState()

    LaunchedEffect(Unit) {
        if (paymentMethods.isEmpty())
            viewModel.loadPaymentMethods() }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        item {
            Text("Payment methods", style = MaterialTheme.typography.headlineSmall)

            // Itera su tutti i metodi di pagamento esistenti
            paymentMethods.forEach { paymentMethod ->
                PaymentMethodRow(
                    paymentMethod = paymentMethod,
                    isSelected = paymentMethod == selectedPaymentMethod,
                    onSelect = { viewModel.selectPaymentMethod(paymentMethod) },
                    onDeleteClick = { viewModel.deletePaymentMethod(paymentMethod.id) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Form per aggiungere un nuovo metodo di pagamento
        item {
            AddPaymentMethodForm(
                isExpanded = viewModel.isAddingNewPaymentMethod.collectAsState().value,
                onExpandChange = { viewModel.toggleAddNewPaymentMethod() },
                cardHolderName = viewModel.cardHolderName.collectAsState().value,
                cardNumber = viewModel.cardNumber.collectAsState().value,
                expirationDate = viewModel.expirationDate.collectAsState().value,
                selectedCardProvider = viewModel.selectedCardProvider.collectAsState().value,
                onCardHolderNameChange = { viewModel.onCardHolderNameChange(it) },
                onCardNumberChange = { viewModel.onCardNumberChange(it) },
                onExpirationDateChange = { viewModel.onExpirationDateChange(it) },
                onCardProviderSelected = { viewModel.selectCardProvider(it) },
                onSaveClick = {
                    viewModel.onAddPaymentMethodClick()
                    navController.navigateUp() // Torna alla schermata precedente
                },
                selectedPaymentMethodType = viewModel.selectedPaymentMethodType.collectAsState().value,
                onPaymentMethodTypeSelected = { viewModel.selectPaymentMethodType(it) }


            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Pulsante per confermare la selezione del metodo di pagamento
        item {
            Button(onClick = { navController.navigateUp() }, modifier = Modifier.fillMaxWidth()) {
                Text("Save Payment Method")
            }
        }
    }
}

@Composable
fun PaymentMethodRow(
    paymentMethod: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDeleteClick: () -> Unit  // Aggiungi callback per cancellare il metodo
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RadioButton(selected = isSelected, onClick = onSelect)

            Column(modifier = Modifier.weight(1f)) {
                Text(paymentMethod.cardHolderName)
                Text(paymentMethod.cardNumber)
                Text(paymentMethod.expirationDate)
            }

            // Pulsante per cancellare il metodo di pagamento
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete payment method")
            }
        }
    }
}


