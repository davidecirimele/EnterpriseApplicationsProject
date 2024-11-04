package com.example.ecommercefront_end.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.SessionManager.user
import com.example.ecommercefront_end.model.UserId
import java.util.UUID

@Composable
fun AccountOptions(userId: UUID?=null, currentRoute: String, navController: NavController){
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(onClick = { navController.navigate("addresses/${userId}"){
                popUpTo(currentRoute) { inclusive = true }
            }}){
                Text(text = "Addresses")
            }
        }
        if (user?.role  == "ROLE_ADMIN"){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    navController.navigate("/admin/wishlist/${userId}") {
                        popUpTo(currentRoute) { inclusive = true }
                    }
                }) {
                    Text(text = "Wishlists")
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(onClick = { navController.navigate("payment-methods"){
                popUpTo(currentRoute) { inclusive = true }
            }}) {
                Text(text = "Payment Methods")
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(onClick = { navController.navigate("transactions")}) {
                Text(text = "Transactions")
            }
        }
    }
}