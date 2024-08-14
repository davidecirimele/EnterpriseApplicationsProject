package com.example.ecommercefront_end.viewmodels

import androidx.compose.runtime.mutableStateOf
import com.example.ecommercefront_end.model.User

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import androidx.compose.runtime.mutableStateOf

data class RegistrationData(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var shippingAddress: ShippingAddress = ShippingAddress()
)

data class ShippingAddress(
    var street: String = "",
    var city: String = "",
    var postalCode: String = "",
    var country: String = ""
)

class RegistrationViewModel : ViewModel() {
    var registrationData = mutableStateOf(RegistrationData())

    // Funzione per aggiornare i dati della registrazione
    fun updateUserDetails(name: String, email: String, password: String) {
        registrationData.value = registrationData.value.copy(
            name = name,
            email = email,
            password = password
        )
    }

    fun updateShippingAddress(street: String, city: String, postalCode: String, country: String) {
        registrationData.value = registrationData.value.copy(
            shippingAddress = ShippingAddress(street, city, postalCode, country)
        )
    }
}
