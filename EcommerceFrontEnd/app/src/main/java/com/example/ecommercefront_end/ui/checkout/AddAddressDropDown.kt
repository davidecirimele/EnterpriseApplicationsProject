package com.example.ecommercefront_end.ui.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddAddressDropDown(
    isExpanded: Boolean,  // Stato che indica se il form è espanso o compresso
    onExpandChange: () -> Unit,  // Callback per cambiare lo stato di espansione
    firstName: String,
    lastName: String,
    _province: String,
    _street: String,
    _city: String,
    _postalCode: String,
    _state: String,
    _additionalInfo: String,
    onStateChange : (String) -> Unit,
    onProvinceChange: (String) -> Unit,
    onStreetChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPostalCodeChange: (String) -> Unit,
    onAdditionalInfoChange: (String) -> Unit,
    isEditing : Boolean
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // RIGA CHE MOSTRA L'INTESTAZIONE DEL FORM E LA FRECCIA PER ESPANDERE/COMPRIMERE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandChange() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!isEditing) {
                Text("Add New Address", style = MaterialTheme.typography.headlineSmall)
            } else {
                Text("Edit Address", style = MaterialTheme.typography.headlineSmall)
            }
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,  // Freccia verso l'alto o il basso in base allo stato
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }


        if (isExpanded) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("First name: $firstName", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Last name: $lastName", style = MaterialTheme.typography.bodyMedium)


            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = _street,
                onValueChange = onStreetChange,
                label = { Text("Address line 1") }
            )

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = _city,
                onValueChange = onCityChange,
                label = { Text("City") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = _postalCode,
                onValueChange = onPostalCodeChange,
                label = { Text("Postcode") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = _province,
                onValueChange = onProvinceChange,
                label = { Text("Province") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = _state,
                onValueChange = onStateChange,
                label = { Text("State") }

            )

            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = _additionalInfo,
                onValueChange = onAdditionalInfoChange,
                label = { Text("Additional Info") }
            )

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}
