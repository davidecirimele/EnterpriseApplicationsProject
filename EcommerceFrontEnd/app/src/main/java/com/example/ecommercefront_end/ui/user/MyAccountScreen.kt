package com.example.ecommercefront_end.ui.user

import android.annotation.SuppressLint
import android.text.style.BackgroundColorSpan
import android.widget.Space
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.SessionManager.user
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.network.RetrofitClient
import com.example.ecommercefront_end.repository.AccountRepository
import com.example.ecommercefront_end.ui.books.BookCover
import com.example.ecommercefront_end.viewmodels.AccountViewModel

@Composable
fun MyAccountScreen(viewModel: AccountViewModel, navHostController: NavHostController) {

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top){
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
        UserInfo()
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Options(navHostController)
        }


    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UserInfo(){
    val address = Address(1,"Test rue", "TS", "Test", "TestLand", "00000", "")
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()){
            Text(
                text =  "${user?.firstName}, ${user?.lastName}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 35.sp
                )
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
                Text(
                    text = "test@test.com",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "edit email", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            Column {
                Text(
                    text = "Phone Number",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 25.sp
                )
                Text(
                    text = "340000093",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "edit phone number", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Column() {
                Text(
                    text = "Default Address",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 25.sp
                )
                AddressView(address = address)
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
fun AddressView(address: Address){
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(8.dp)){
        Column {
            Text(
                text = "Street: ${address.street}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "City: ${address.city}, ZIP: ${address.postalCode}, Province: ${address.province}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "State: ${address.state}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Edit")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Remove")
                }
            }
        }
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
fun AddressesList(){
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
        Text(
            text = "Saved addresses",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 25.sp
        )
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(state = ScrollState(1))
        ) {
            addresses.forEach { address ->
                AddressView(address)
            }

        }
    }
}


@Preview
@Composable
fun MyAccountScreenPreview(){
    val _userApiService = RetrofitClient.userApiService
    val repository = AccountRepository(_userApiService)
    val viewModel = AccountViewModel(repository)
    val navController = rememberNavController()
    MyAccountScreen(viewModel, navController)
}