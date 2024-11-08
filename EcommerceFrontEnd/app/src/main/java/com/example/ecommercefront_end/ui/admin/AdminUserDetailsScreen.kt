package com.example.ecommercefront_end.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.ui.user.AccountOptions
import com.example.ecommercefront_end.viewmodels.AccountViewModel

@Composable
fun AdminUserDetailsScreen(accountViewModel: AccountViewModel, user: UserDetails, navHostController: NavHostController, onDelete: ()-> Unit){

    Card(modifier = Modifier.fillMaxSize().background(color = Color.Gray)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row {
                        Text(
                            text = "ID",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = user.id.toString()?:"Null",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row {
                        Text(
                            text = "Name",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = user.firstName?:"Null",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row {
                        Text(
                            text = "Surname",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = user.lastName?:"Null",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row {
                        Text(
                            text = "Email",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = user.email?:"Null",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row {
                        Text(
                            text = "Phone Number",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = user.phoneNumber?:"Null",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AccountOptions(user.id,"/admin/user_details/${user.id}",navHostController)
                }
            }


        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(
                onClick = { accountViewModel.deleteUser(userId = user.id, onDelete = {onDelete(); SessionManager.clearSession()}) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 18.dp)
            ) {
                Text("Delete user")
            }
        }
    }
}