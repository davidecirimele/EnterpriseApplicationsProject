package com.example.ecommercefront_end.ui.user.Address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import java.util.UUID

@Composable
fun InsertAddressScreen(viewModel: AddressViewModel, navController: NavHostController, userId: UUID? = null) {

    var street by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

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

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Insert Address") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    },snackbarHost = { SnackbarHost(viewModel.snackbarHostState) }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                OutlinedTextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Street") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = province,
                    onValueChange = {
                        province = it
                    },
                    label = { Text("Province") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = state,
                    onValueChange = {
                        state = it
                    },
                    label = { Text("State") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = postalCode,
                    onValueChange = {
                        postalCode = it
                    },
                    label = { Text("ZIP Code") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = additionalInfo,
                    onValueChange = {
                        additionalInfo = it
                    },
                    label = { Text("Additional Info") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = false,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {

                        viewModel.insertAddress(
                            userId,
                            SaveAddress(
                                street,
                                province,
                                city,
                                state,
                                postalCode,
                                additionalInfo
                            ), onSuccess = {navController.popBackStack()}
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = street.isNotBlank() && province.isNotBlank() && city.isNotBlank() && state.isNotBlank() && postalCode.isNotBlank()
                ) {
                    Text("Save", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}