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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import kotlinx.coroutines.launch

@Composable
fun MyAccountScreen(accountViewModel: AccountViewModel, addressViewModel: AddressViewModel, navHostController: NavHostController) {

    val userDetails by accountViewModel.userDetails.collectAsState()

    val defaultAddress by addressViewModel.defaultAddress.collectAsState()


    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp), verticalArrangement = Arrangement.Top){
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "My Account",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 40.sp
                )
            }
        }
        item{
        Spacer(modifier = Modifier.height(20.dp))
        }
        item{
        UserInfo(userDetails, defaultAddress,  accountViewModel = accountViewModel, addressViewModel, navHostController)
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Options(navHostController)
        }


    }
}

@Composable
fun UserInfo(userDetails: UserDetails?,defaultAddress: Address?, accountViewModel : AccountViewModel, addressViewModel: AddressViewModel, navController: NavHostController){

    var isEditingEmail by remember { mutableStateOf(false) }

    var isEditingPhoneNumber by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()){
            if (userDetails != null) {
                Text(
                    text =  "${userDetails.firstName} ${userDetails.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 35.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            Column {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 25.sp
                )
                if (userDetails != null) {
                    Text(
                        text = "${userDetails.email}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 25.sp
                    )
                }
            }
            IconButton(onClick = {  isEditingEmail = true }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "edit email", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            EditEmailScreen(isEditingEmail, accountViewModel = accountViewModel, onSuccess = {
                    isEditingEmail = false
                    navController.navigate("userAuth") {
                        popUpTo("userAuth") { inclusive = true }
                    }
            })
        }
        Spacer(modifier = Modifier.height(30.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()){
                Column {
                    Text(
                        text = "Phone Number",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 25.sp
                    )
                    if (userDetails != null) {
                        Text(
                            text = "${userDetails.phoneNumber}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp
                        )
                    }
                }
                IconButton(onClick = { isEditingPhoneNumber = true }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "edit phone number",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                EditPhoneNumberScreen(isEditingPhoneNumber, accountViewModel = accountViewModel, onSuccess = {
                    isEditingPhoneNumber = false
                    navController.navigate("my-account") {
                    popUpTo("my-account") { inclusive = true }
                }
            })
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Column() {
                if(defaultAddress != null) {
                    Text(
                        text = "Default Address:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 25.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AddressView(address = defaultAddress, addressViewModel, navController)
                }
                else
                    Text(
                        text = "No Default Address",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 25.sp
                    )
            }
        }
            /*user?.phoneNumber?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 20.sp
                )
            }*/
    }
}

@Composable
fun Options(navHostController: NavHostController){
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(onClick = { navHostController.navigate("addresses") }) {
                Text(text = "Addresses")
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Payment Methods")
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Transactions")
            }
        }
    }

}

@Composable
fun EditEmailScreen(isEditing: Boolean, accountViewModel: AccountViewModel, onSuccess: () -> Unit) {
    var inputText by remember { mutableStateOf("") }
    Column {

        // Se `isEditing` è true, mostra il campo di testo e il bottone "Submit"
        if (isEditing) {
            Spacer(modifier = Modifier.height(16.dp)) // Spazio tra i componenti

            // Campo di testo per inserire le modifiche
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text(text = "Enter your new email") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spazio tra i componenti

            // Bottone "Submit"
            Button(
                onClick = {
                    accountViewModel.viewModelScope.launch {
                        accountViewModel.editEmail(inputText, onSuccess)
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Composable
fun EditPhoneNumberScreen(isEditing: Boolean, accountViewModel: AccountViewModel, onSuccess: () -> Unit) {
    var inputText by remember { mutableStateOf("") }
    Column {

        // Se `isEditing` è true, mostra il campo di testo e il bottone "Submit"
        if (isEditing) {
            Spacer(modifier = Modifier.height(16.dp)) // Spazio tra i componenti

            // Campo di testo per inserire le modifiche
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text(text = "Enter your new Phone Number") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spazio tra i componenti

            // Bottone "Submit"
            Button(
                onClick = {
                    accountViewModel.viewModelScope.launch {
                        accountViewModel.editPhoneNumber(inputText, onSuccess)
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Submit")
            }
        }
    }
}
