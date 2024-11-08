package com.example.ecommercefront_end.ui.user

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import com.example.ecommercefront_end.model.Credential
import com.example.ecommercefront_end.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun SignInUpScreen(
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    navController: NavController
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbarLogin by loginViewModel.showSnackbar.collectAsState()
    val showSnackbarRegistration by registrationViewModel.showSnackbar.collectAsState()

    val snackbarMessageLogin by loginViewModel.snackbarMessage.collectAsState()
    val snackbarMessageRegistration by registrationViewModel.snackbarMessage.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Sign in") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Sign up") }
                )
            }

            when (selectedTabIndex) {
                0 -> LoginPage(
                    loginViewModel = loginViewModel,
                    onLoginSuccess = {
                        // Trigger the snackbar and wait for it to show, then navigate
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar(snackbarMessageLogin)
                            navController.navigate("home") {
                                popUpTo("userAuth") { inclusive = true }
                            }
                        }
                    }
                )

                1 -> RegistrationScreen(
                    registrationViewModel,
                    onRegistrationComplete = {
                        // Trigger the snackbar and wait for it to show, then navigate
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar(snackbarMessageRegistration)
                            navController.navigate("userAuth") {
                                popUpTo("userAuth") { inclusive = true }
                            }
                        }
                    },
                    onSwitchToLogin = { selectedTabIndex = 0 }
                )
            }
        }

        // LaunchedEffect to show the Snackbar
        LaunchedEffect(showSnackbarLogin, showSnackbarRegistration) {
            Log.d("Snackbar", "showSnackbarLogin: $showSnackbarLogin, showSnackbarRegistration: $showSnackbarRegistration")
            if (showSnackbarLogin) {
                snackbarHostState.showSnackbar(
                    message = snackbarMessageLogin,
                    duration = SnackbarDuration.Short
                )
                Log.d("Snackbar", "showSnackbarLogin: $showSnackbarLogin, showSnackbarRegistration: $showSnackbarRegistration")

                loginViewModel.setShowSnackbar(false) // Reset state for next message
            } else if (showSnackbarRegistration) {
                snackbarHostState.showSnackbar(
                    message = snackbarMessageRegistration,
                    duration = SnackbarDuration.Short
                )
                Log.d("Snackbar", "showSnackbarLogin: $showSnackbarLogin, showSnackbarRegistration: $showSnackbarRegistration")

                registrationViewModel.setShowSnackbar(false) // Reset state for next message
            }
        }
    }
}


@Composable
fun LoginPage(loginViewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isEmailValid by remember { mutableStateOf(false) }
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    var isPasswordValid by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = emailRegex.matches(it)
            },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordValid =
                    it.length in 8..20   // Controlla se la password ha almeno 8 caratteri
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 8.dp),

            shape = RoundedCornerShape(16.dp),
            singleLine = true,
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
            onClick = {
                loginViewModel.login(Credential(email, password), onLoginSuccess)
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(bottom = 16.dp),
            enabled = isEmailValid && isPasswordValid
        ) {
            Text("Login", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun RegistrationScreen(registrationViewModel : RegistrationViewModel ,onRegistrationComplete: () -> Unit, onSwitchToLogin: () -> Unit) {
    var currentStep by remember { mutableStateOf(1) }

    Column(modifier = Modifier.padding(16.dp)) {
        when (currentStep) {
            1 -> RegistrationStep1(registrationViewModel, onNext = { currentStep = 2 })
            2 -> RegistrationStep2(registrationViewModel = registrationViewModel, onRegistrationComplete, onNext = { currentStep = 3 }, onBack = { currentStep = 1 })
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (currentStep == 1) {
            TextButton(onClick = onSwitchToLogin) {
                Text("Already have an account? Sign in!")
            }
        }
    }
}

@Composable
fun RegistrationStep1(registrationViewModel: RegistrationViewModel, onNext: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("")}
    var passwordVisible by remember { mutableStateOf(false) }

    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isConfirmPasswordValid by remember { mutableStateOf(true) }

    // Regex per la validazione dell'email
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // Regex per la validazione della password
    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[\\w@#$%^&+=!]{8,20}$")

    val isFormValid = isEmailValid && isPasswordValid && isConfirmPasswordValid
            && name.isNotBlank() && surname.isNotBlank() && email.isNotBlank()
            && password.isNotBlank() && confirmPassword.isNotBlank()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Step 1 / 2", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isEmailValid = emailRegex.matches(it)
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = !isEmailValid
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordValid = passwordRegex.matches(it)
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Nascondi password" else "Mostra password"
                        )
                    }
                },

                isError = !isPasswordValid // Mostra un errore se la password non è valida
            )
            Text(
                text = "The password must be between 8 and 20 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .offset(x = 8.dp),
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    isConfirmPasswordValid = (it == password) // Controlla se le password corrispondono
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                                            else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Nascondi password"
                                                else "Mostra password"
                        )
                    }
                },
                isError = !isConfirmPasswordValid // Mostra un errore se le password non corrispondono
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        registrationViewModel.updateUserDetails(name, surname, email, password)
                        onNext() // Passa al prossimo step
                    } else {
                        // Mostra un messaggio di errore: le password non corrispondono
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                 enabled = isFormValid
            ) {
                Text("Next step ->", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationStep2(registrationViewModel: RegistrationViewModel, onRegistrationComplete: () -> Unit, onNext: () -> Unit, onBack: () -> Unit) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var admin by remember { mutableStateOf("") }

    fun isValidDate(date: LocalDate): Boolean = date.isBefore(LocalDate.now())

    var isPhoneNumberValid by remember { mutableStateOf(false) }
    val italianPhoneNumberRegex = Regex("[0-9]{9,11}$")

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
            Text("Step 2 / 2", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(10.dp))

            // Data di nascita
            OutlinedTextField(
                value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                onValueChange = { },
                label = { Text("Birth Date") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Select data")
                    }
                }
            )
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },confirmButton = {
                        Button(onClick = { showDatePicker = false }) {
                            Text("OK")
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault())
                            .toInstant().toEpochMilli(),
                        yearRange = IntRange(1900, LocalDate.now().year),
                        selectableDates = object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                return Instant.ofEpochMilli(utcTimeMillis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .isBefore(LocalDate.now()) ||
                                        Instant.ofEpochMilli(utcTimeMillis)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                                            .isEqual(LocalDate.now())
                            }
                        }
                    )

                    // Aggiorna selectedDate quando la data selezionata cambia
                    LaunchedEffect(datePickerState.selectedDateMillis) {
                        if (datePickerState.selectedDateMillis != null) {
                            selectedDate = Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                    }

                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Numero di telefono
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    phoneNumber = newValue.filter { it.isDigit() } // Accetta solo numeri
                    isPhoneNumberValid =  phoneNumber.isNotBlank() && italianPhoneNumberRegex.matches(phoneNumber)
                },
                label = { Text("Phone number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = admin,
                onValueChange = { admin = it },
                label = { Text("ADMIN Code") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
            Text(
                text = "Insert Admin Code",
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
                registrationViewModel.updateUserDetails(selectedDate, phoneNumber, admin)
                registrationViewModel.register(onRegistrationComplete)
                onNext() // Passa al prossimo step
            },
                enabled = isPhoneNumberValid
            ) {
                Text("Complete Sing Up")
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


