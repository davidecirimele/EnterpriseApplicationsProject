package com.example.ecommercefront_end.ui.user

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecommercefront_end.model.PasswordUser
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import java.util.UUID

@Composable
fun ChangePasswordScreen(viewModel: AccountViewModel, navController: NavHostController, userId: UUID?=null) {

    var oldpassword by remember{mutableStateOf("")}
    var newpassword by remember{mutableStateOf("")}
    var confirmedpassword by remember{mutableStateOf("")}

    var showClearPassword by remember{mutableStateOf(false)}

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("Change Password") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    }) {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                ) {

                Text(text = "The password must be between 8 and 20 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character",
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = oldpassword,
                    onValueChange = { oldpassword = it },
                    label = { Text("Old Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = if (showClearPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showClearPassword = !showClearPassword }) {
                            Icon(
                                imageVector = if (showClearPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showClearPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = newpassword,
                    onValueChange = { newpassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = if (showClearPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showClearPassword = !showClearPassword }) {
                            Icon(
                                imageVector = if (showClearPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showClearPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = confirmedpassword,
                    onValueChange = { confirmedpassword = it },
                    label = { Text("Confirm new Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    visualTransformation = if (showClearPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showClearPassword = !showClearPassword }) {
                            Icon(
                                imageVector = if (showClearPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showClearPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.changePassword(PasswordUser(oldPassword = oldpassword, newPassword = newpassword))

                        navController.popBackStack()
                              },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = oldpassword.isNotBlank() && newpassword.isNotBlank() && confirmedpassword.isNotBlank() && (newpassword == confirmedpassword)
                ) {
                    Text("Change Password", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}