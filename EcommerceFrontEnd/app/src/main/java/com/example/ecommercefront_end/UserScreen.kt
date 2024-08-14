package com.example.ecommercefront_end

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommercefront_end.viewmodels.RegistrationViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAuthScreen(navController: NavController) {
    var isLoginScreen by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { isLoginScreen = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLoginScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    "Accedi",
                    color = if (isLoginScreen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { isLoginScreen = false },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isLoginScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    "Registrati",
                    color = if (!isLoginScreen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Il resto del codice per visualizzare LoginPage o RegistrationScreen
        if (isLoginScreen) {
            LoginPage(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("userAuth") { inclusive = true }
                    }
                },
                onSwitchToRegister = { isLoginScreen = false }
            )
        } else {
            RegistrationScreen(
                onRegistrationComplete = {
                    navController.navigate("home") {
                        popUpTo("userAuth") { inclusive = true }
                    }
                },
                onSwitchToLogin = { isLoginScreen = true }
            )
        }
    }
}

@Composable
fun LoginPage(onLoginSuccess: () -> Unit, onSwitchToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bentornato!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Nascondi password" else "Mostra password"
                    )
                }
            }
        )

        Button(
            onClick = { /* Logica di login */ },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 16.dp)
        ) {
            Text("Accedi", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun RegistrationScreen(onRegistrationComplete: () -> Unit, onSwitchToLogin: () -> Unit) {
    var currentStep by remember { mutableStateOf(1) }

    Column(modifier = Modifier.padding(16.dp)) {
        when (currentStep) {
            1 -> RegistrationStep1(onNext = { currentStep = 2 })
            2 -> RegistrationStep2(onNext = { currentStep = 3 }, onBack = { currentStep = 1 })
            3 -> RegistrationStep3(onComplete = onRegistrationComplete, onBack = { currentStep = 2 })
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (currentStep == 1) {
            TextButton(onClick = onSwitchToLogin) {
                Text("Hai già un account? Accedi")
            }
        }
    }
}
@Composable
fun RegistrationStep1(onNext: () -> Unit) {
    Column {
        Text("Registrazione - Step 1")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNext) {
            Text("Avanti")
        }
    }
}

@Composable
fun RegistrationStep2(onNext: () -> Unit, onBack: () -> Unit) {
    Column {
        Text("Registrazione - Step 2")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNext) {
            Text("Avanti")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onBack) {
            Text("Indietro")
        }
    }
}

@Composable
fun RegistrationStep3(onComplete: () -> Unit, onBack: () -> Unit) {
    Column {
        Text("Registrazione - Step 3")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onComplete) {
            Text("Completa Registrazione")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onBack) {
            Text("Indietro")
        }
    }
}

/*
@Composable
fun UserDetailsScreen(navController: NavController, viewModel: RegistrationViewModel) {
    var name by remember { mutableStateOf(viewModel.registrationData.value.name) }
    var email by remember { mutableStateOf(viewModel.registrationData.value.email) }
    var password by remember { mutableStateOf(viewModel.registrationData.value.password) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dettagli Utente", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome e Cognome") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Nascondi password" else "Mostra password"
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )

        Button(
            onClick = {
                // Salva i dati nel ViewModel
                viewModel.registrationData.value = viewModel.registrationData.value.copy(
                    name = name,
                    email = email,
                    password = password
                )
                // Naviga alla schermata successiva
                navController.navigate("shippingAddress")
            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continua")
        }
    }
}*/

/*
@Composable
fun RegistrationCompleteScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrazione Completa!", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        Text("Grazie per esserti registrato. Puoi ora accedere alla tua area personale.", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo("userStart") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Vai al Login")
        }
    }
}
*/

/*
@Composable
fun UserScreen(navHostController: NavHostController) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "userStart") {
        composable("userStart") { UserStartScreen(navController) }
        composable("login") { LoginPage() }
        // Aggiungi le rotte per i passaggi di registrazione qui
    }
}*/


