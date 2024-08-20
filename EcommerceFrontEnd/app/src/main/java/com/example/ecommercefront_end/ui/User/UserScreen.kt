package com.example.ecommercefront_end.ui.user

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAuthScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {Tab(
            selected = selectedTabIndex == 0,
            onClick = { selectedTabIndex = 0 },
            text = { Text("Accedi") }
        )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Registrati") }
            )
        }

        // Il resto del codice per visualizzare LoginPage o RegistrationScreen
        when (selectedTabIndex) {
            0 -> LoginPage(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("userAuth") { inclusive = true }
                    }
                },
                onSwitchToRegister = { selectedTabIndex = 1 }
            )
            1 -> RegistrationScreen(
                onRegistrationComplete = {
                    navController.navigate("home") {
                        popUpTo("userAuth") { inclusive = true }
                    }
                },
                onSwitchToLogin = { selectedTabIndex = 0 }
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
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp),
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
        Spacer(modifier = Modifier.height(10.dp))

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
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("")}
    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Step 1 su 2", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Cognome") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Nascondi password" else "Mostra password"
                        )
                    }
                }
            )
            Text(
                text = "La password deve contenere almeno 8 caratteri, una lettera maiuscola, una lettera minuscola e un numero",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .offset(x = 8.dp),
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Conferma Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Nascondi password" else "Mostra password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        // Salva i dati nel ViewModel o esegui la logica di registrazione
                        onNext() // Passa al prossimo step
                    } else {
                        // Mostra un messaggio di errore: le password non corrispondono
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verifica Email", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationStep2(onNext: () -> Unit, onBack: () -> Unit) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var admin by remember { mutableStateOf("") }

    /*
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }*/


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Step 2 su 2", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(10.dp))

            // Data di nascita
            OutlinedTextField(
                value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                onValueChange = { },
                label = { Text("Data di nascita") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Seleziona data")
                    }
                }
            )
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Button(onClick = { showDatePicker = false }) {
                            Text("OK")
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Annulla")
                        }
                    }
                ) {
                    DatePicker(
                        state = rememberDatePickerState(
                            initialSelectedDateMillis = selectedDate.atStartOfDay(
                                ZoneId.systemDefault()
                            ).toInstant().toEpochMilli()
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Numero di telefono
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    phoneNumber = newValue.filter { it.isDigit() } // Accetta solo numeri
                },
                label = { Text("Numero di telefono") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = admin,
                onValueChange = { admin = it },
                label = { Text("Codice ADMIN") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Text(
                text = "Facoltativo",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .offset(x = 8.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))


        /* Indirizzo
        OutlinedTextField(
            value = street,
            onValueChange = { street = it },
            label = { Text("Via") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Città") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = province,
            onValueChange = { province = it },
            label = { Text("Provincia") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = zipCode,
            onValueChange = { zipCode = it },
            label = { Text("Codice postale") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        */


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onBack) {
                Text("Indietro")
            }

            Button(onClick = {
                // Salva i dati nel ViewModel o esegui la logica di registrazione
                onNext() // Passa al prossimo step
            }) {
                Text("Completa Registrazione")
            }
        }
      }
    }
}

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
}

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


