package com.example.ecommercefront_end.ui.user.Address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.utils.insertButton
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import java.util.UUID


@Composable
fun AddressesScreen(userId: UUID?=null, viewModel: AddressViewModel, navHostController: NavHostController) {

    val addresses by viewModel.addresses.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Saved Addresses") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f)) {
                if (addresses != null && !addresses!!.isEmpty())
                    for (address in addresses!!)
                        item {
                            AddressView(
                                address = address,
                                userId,
                                viewModel,
                                navHostController,
                                true
                            )
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


