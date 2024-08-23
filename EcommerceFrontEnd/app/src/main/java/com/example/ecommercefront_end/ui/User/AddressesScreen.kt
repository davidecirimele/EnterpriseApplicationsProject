package com.example.ecommercefront_end.ui.user

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AddressViewModel


@Composable
fun AddressesScreen(viewModel: AddressViewModel, navHostController: NavHostController) {
    val address1 = Address(1,"Test rue 1", "TS", "Test", "TestLand", "00000", "")
    val address2 = Address(2,"Test rue 2", "TS", "Test", "TestLand", "00000", "")
    val address3 = Address(3,"Test rue 3", "TS", "Test", "TestLand", "00000", "")
    val address4 = Address(4,"Test rue 4", "TS", "Test", "TestLand", "00000", "")
    val addresses = listOf(
        address1,
        address2,
        address3,
        address4
    )

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = "Saved Addresses: ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 30.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            for (address in addresses)
                item {
                    AddressView(address = address)
                }
        }
    }

}

    @Preview
    @Composable
    fun AddressesScreenPreview(){
        val _addressApiService = RetrofitClient.addressApiService
        val repository = AddressRepository(_addressApiService)
        val viewModel = AddressViewModel(repository)
        val navController = rememberNavController()
        AddressesScreen(viewModel, navHostController = navController)
    }
