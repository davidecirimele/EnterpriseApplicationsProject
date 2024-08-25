package com.example.ecommercefront_end.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.viewmodels.AddressViewModel

@Composable
fun AddressView(address: Address, addressViewModel: AddressViewModel, navController: NavHostController){

    var checked by remember { mutableStateOf(address.defaultAddress) }

    var showDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(4.dp)){
        Column (modifier = Modifier.padding(8.dp)){
            Row() {
                Column() {
                    Text(
                        text = "Address: ${address.street}, ${address.city}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ZIP: ${address.postalCode}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Province: ${address.province}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "State: ${address.state}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button(onClick = { navController.navigate("edit-address") }) {
                    Text(text = "Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showDialog = true }) {
                    Text(text = "Remove")
                }
                if(!address.defaultAddress){
                    Spacer(modifier = Modifier.width(8.dp))
                    defaultCheckBox(checked, addressViewModel, address, navController)
                    {
                        checked = it
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Removal") },
            text = { Text("Are you sure you want to remove this address?") },
            confirmButton = {
                Button(
                    onClick = {
                        addressViewModel.removeAddress(address)
                        showDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun defaultCheckBox(checked : Boolean, addressViewModel: AddressViewModel, address: Address, navController: NavHostController, onCheckedChange: (Boolean) -> Unit){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set as default",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp,
            modifier = Modifier.padding(2.dp)
        )
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
                addressViewModel.setDefaultAddress(address)
                navController.navigate("my-account")
            },
            modifier = Modifier.padding(2.dp)
        )
    }
}

/*@Preview
@Composable
fun addressViewPreview(){
    val _addressApiService = RetrofitClient.addressApiService
    val repository = AddressRepository(_addressApiService)
    val viewModel = AddressViewModel(repository)
    val navController = rememberNavController()
    AddressView(Address(1,"Via Test, 1", "Tester", "TestCity", "Testate", "80190", "", false), viewModel, navController)
}*/