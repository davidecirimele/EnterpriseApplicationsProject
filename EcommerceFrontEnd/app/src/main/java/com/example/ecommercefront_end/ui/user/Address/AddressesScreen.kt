package com.example.ecommercefront_end.ui.user.Address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.utils.insertButton
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import java.util.UUID


@Composable
fun AddressesScreen(userId: UUID?=null, viewModel: AddressViewModel, navHostController: NavHostController) {

    val addresses by viewModel.addresses.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            val result = viewModel.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "RETRY",
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                val id = userId ?: SessionManager.getUser().userId
                viewModel.fetchUserAddresses(id)
            }
            else if (result == SnackbarResult.Dismissed) {
                viewModel.onSnackbarDismissed()
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Saved Addresses") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    },snackbarHost = { SnackbarHost(viewModel.snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f)) {
                if (addresses.isNotEmpty())
                    for (address in addresses)
                        item {
                            AddressView(
                                address = address,
                                userId,
                                viewModel,
                                navHostController,
                                true
                            )
                        }
                else
                {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No addresses saved")
                        }
                    }
                }
            }
            insertButton(userId, navHostController, onButtonClicked = {navHostController.navigate("insert-address/$userId") {
                popUpTo("addresses/$userId") {
                    saveState = true
                }
            }})
        }
    }
}


