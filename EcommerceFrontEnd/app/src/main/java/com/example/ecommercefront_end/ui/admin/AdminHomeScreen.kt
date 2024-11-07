package com.example.ecommercefront_end.ui.admin

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
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
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

//samu@gm.com   Ciaobello!10
//lollo@gm.com   Ciaobello!10
//admin@gm.com  Ciaobello!10



@Composable
fun AdminHomeScreen(navHostController: NavHostController){
    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Home") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
            {
                Column {
                    Row {
                        optionButton(onButtonClicked = {
                            navHostController.navigate("admin-catalogue") {
                                popUpTo("admin-home") { saveState = true }
                            }
                        }, icon = Icons.Default.Book, contentDescription = "Catalogue")

                        Spacer(modifier = Modifier.padding(10.dp))

                        optionButton(onButtonClicked = {
                            navHostController.navigate("admin-users-list") {
                                popUpTo("admin-home") { saveState = true }
                            }
                        }, icon = Icons.Default.PersonSearch, contentDescription = "Users List")
                    }

                    Spacer(modifier = Modifier.padding(vertical = 16.dp))

                    Row {
                        optionButton(onButtonClicked = {
                            navHostController.navigate("admin-orders") {
                                popUpTo("admin-home") { saveState = true }
                            }
                        }, icon = Icons.Default.Payments, contentDescription = "Orders")

                        Spacer(modifier = Modifier.padding(10.dp))

                        optionButton(onButtonClicked = {
                            navHostController.navigate("my-account") {
                                popUpTo("account-manager") { saveState = true }
                            }
                        }, icon = Icons.Default.Reviews, contentDescription = "Reviews")
                    }
                }
            }
        }
    }
}

@Composable
fun optionButton(onButtonClicked : () -> Unit, icon: ImageVector, contentDescription: String){
    Card(modifier = Modifier.width(150.dp).height(150.dp)
        .clickable(onClick = {
            onButtonClicked();
        })
    ) {

        Text(contentDescription, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally))
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(50.dp).align(Alignment.CenterHorizontally)
        )
    }
}