package com.example.ecommercefront_end.ui.user.Payments

import CheckoutViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommercefront_end.R
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.utils.insertButton
import java.util.UUID


@Composable
fun PaymentMethodsScreen(userId: UUID?=null, viewModel: CheckoutViewModel, navController: NavController) {

    val paymentMethods by viewModel.paymentMethods.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            val result = viewModel.snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
                actionLabel = "RETRY"
            )

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.loadPaymentMethods()
            }
            else if (result == SnackbarResult.Dismissed) {
                viewModel.onSnackbarDismissed()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPaymentMethods()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Saved Payment Methods") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    },snackbarHost = { SnackbarHost(viewModel.snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {
                item {
                    if (paymentMethods.isNotEmpty())
                        paymentMethods.forEach { paymentMethod ->
                            PaymentMethodCard(
                                paymentMethod = paymentMethod,
                                onDeleteClick = { viewModel.deletePaymentMethod(paymentMethod.id) }
                            )
                        }
                }

            }

            insertButton(userId, navController, onButtonClicked = {navController.navigate("insert-payment-method") {
                popUpTo("my-account") {
                    saveState = true
                }
            }})

        }
    }
}

@Composable
fun PaymentMethodCard(paymentMethod: PaymentMethod, onDeleteClick: ()->Unit){
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(paymentMethod.cardHolderName)
                Text(paymentMethod.cardNumber)
                Text(paymentMethod.expirationDate)
            }
            CreditCardImage(paymentMethod.provider.toString())


            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete payment method")
            }
        }
    }
}

@Composable
fun CreditCardImage(cardType: String) {
    Log.d("PAYMENT", "CreditCardImage: $cardType")
    val imageResId = when (cardType) {
        "VISA" -> R.drawable.visa
        "MASTERCARD" -> R.drawable.mastercard
        "AMERICAN_EXPRESS" -> R.drawable.american_express
        "MAESTRO" -> R.drawable.maestro
        else -> R.drawable.default_card
    }

    Image(
        painter = painterResource(id = imageResId),
        contentDescription = "$cardType Credit Card",
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp),
        Alignment.Center,
        contentScale = ContentScale.Inside
    )
}
