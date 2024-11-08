package com.example.ecommercefront_end.ui.user

import android.app.Activity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.PasswordUser
import com.example.ecommercefront_end.model.UserDetails
import com.example.ecommercefront_end.ui.user.Address.AddressView
import com.example.ecommercefront_end.viewmodels.AccountViewModel
import com.example.ecommercefront_end.viewmodels.AddressViewModel
import kotlinx.coroutines.launch

@Composable
fun MyAccountScreen(accountViewModel: AccountViewModel, addressViewModel: AddressViewModel, navController: NavController, onLogout: ()->Unit) {

    val userDetails by accountViewModel.userDetails.collectAsState()

    val defaultAddress by addressViewModel.defaultAddress.collectAsState()

    val isDeletingAccount by accountViewModel.isDeletingAccount.collectAsState()

    var showDialog by remember{mutableStateOf(false)}

    val accountErrorMessage by accountViewModel.errorMessage.collectAsState()

    val addressErrorMessage by accountViewModel.errorMessage.collectAsState()

    LaunchedEffect(accountErrorMessage) {
        accountErrorMessage?.let { message ->
            val result = accountViewModel.snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
                withDismissAction = false,
            )

            if (result == SnackbarResult.Dismissed) {
                accountViewModel.onSnackbarDismissed()
            }
        }
    }

    LaunchedEffect(addressErrorMessage) {
        addressErrorMessage?.let { message ->
            val result = addressViewModel.snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
                withDismissAction = false,
            )

            if (result == SnackbarResult.Dismissed) {
                addressViewModel.onSnackbarDismissed()
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { androidx.compose.material.Text("My Account") },
            backgroundColor = Color(0xFF1F1F1F),
            contentColor = Color.White
        )
    },snackbarHost = { SnackbarHost(accountViewModel.snackbarHostState) }) { paddingValues ->
        if (isDeletingAccount) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), verticalArrangement = Arrangement.SpaceAround
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    UserInfo(
                        userDetails,
                        defaultAddress,
                        accountViewModel = accountViewModel,
                        addressViewModel,
                        navController,
                        onLogout
                    )
                }
                if (SessionManager.user != null && SessionManager.user!!.role != "ROLE_ADMIN") {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item {
                        AccountOptions(userDetails?.id, "my-account", navController)
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                showDialog = true
                            }) {
                                Text(text = "Delete Account")
                            }
                        }
                    }
                }
            }
            if (showDialog) {
                val context = LocalContext.current
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "We’re sorry to hear that you’re thinking of leaving us :(") },
                    text = {
                        Column {
                            Text(
                                text = "Deleting your account will permanently remove access to all your data.\n" + "" +
                                        "Are you sure you want to proceed?",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                SessionManager.getUser().let {
                                    accountViewModel.deleteUser(it.userId, onDelete = {
                                        onLogout()
                                        SessionManager.clearSession()
                                    })
                                }
                                navController.navigate("home"){
                                    popUpTo(0) {inclusive= true}
                                }
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
    }
}

@Composable
fun UserInfo(userDetails: UserDetails?,defaultAddress: Address?,accountViewModel: AccountViewModel, addressViewModel: AddressViewModel, navController: NavController, onLogout: () -> Unit){
    var isEditingEmail by remember { mutableStateOf(false) }

    var isEditingPhoneNumber by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                isEditingPhoneNumber = false
                isEditingEmail = false
            })
        }) {
        Row(modifier = Modifier.fillMaxWidth()){
            if (userDetails != null) {
                Text(
                    text =  "${userDetails.firstName} ${userDetails.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            Column {
                Text(
                    text = "Email",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Row() {
                    if (userDetails != null) {
                        Text(
                            text = "${userDetails.email}",
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 15.dp))
                    Icon(modifier = Modifier.clickable{ isEditingEmail = true }, imageVector = Icons.Filled.Edit, contentDescription = "edit email")
                }
            }

        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            EditScreen("Email", isEditingEmail, accountViewModel, onSubmit = {isEditingEmail=false; onLogout(); SessionManager.clearSession(); navController.navigate("userAuth"){
                popUpTo(0) {inclusive= true}
            }})
        }
        Spacer(modifier = Modifier.height(30.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()){
                Column {
                    Text(
                        text = "Phone Number",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Row {
                        if (userDetails != null) {
                            Text(
                                text = "${userDetails.phoneNumber}",
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 15.dp))
                        Icon(modifier = Modifier.clickable{ isEditingPhoneNumber = true }, imageVector = Icons.Filled.Edit, contentDescription = "edit phone number")
                    }

                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                EditScreen("Phone Number", isEditingPhoneNumber, accountViewModel, onSubmit = {isEditingPhoneNumber = false; accountViewModel.loadUserDetails()})
            }
        }
        if(SessionManager.user != null && SessionManager.user!!.role!="ROLE_ADMIN") {
            Spacer(modifier = Modifier.height(30.dp))
            Row {
                Column() {
                    if (defaultAddress != null) {
                        Text(
                            text = "Default Address:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (userDetails != null) {
                            AddressView(
                                address = defaultAddress,
                                userDetails.id,
                                addressViewModel,
                                navController,
                                false
                            )
                        }
                    } else
                        Text(
                            text = "No Default Address",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                }
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
fun EditScreen(key: String, isEditing: Boolean, accountViewModel: AccountViewModel, onSubmit: ()->Unit) {
    var inputText by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false)}

    Column{

        if (isEditing) {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text(text = "Enter your new $key") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Bottone "Submit"
            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Submit")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm $key") },
            text = { Column {
                Text(
                    text = "Are you sure you want to change this $key?",
                    fontWeight = FontWeight.Bold
                )
                if (key == "Email") {
                    Text(
                        text = "You will be asked to log in again",
                        fontWeight = FontWeight.Bold
                    )
                }
            }},
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        accountViewModel.editFunction(key, inputText, onSuccess = onSubmit)
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





