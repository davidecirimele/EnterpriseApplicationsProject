package com.example.ecommercefront_end.ui.checkout

import CheckoutViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.SaveAddress

@Composable
fun CheckoutAddressScreen(viewModel: CheckoutViewModel, navController: NavController) {
    val addresses by viewModel.addresses.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()  // Indirizzo attualmente selezionato
    val isDropDownExpanded by viewModel.isDropDownExpanded.collectAsState()
    val street by viewModel.street.collectAsState()
    val province by viewModel.province.collectAsState()
    val city by viewModel.city.collectAsState()
    val postalCode by viewModel.postalCode.collectAsState()
    val state by viewModel.state.collectAsState()
    val additionalInfo by viewModel.additionalInfo.collectAsState()
    val addressBeingEdited by viewModel.addressBeingEdited.collectAsState()

    LaunchedEffect(Unit) {
        if (addresses.isEmpty())  // Se non ci sono indirizzi, carica quelli esistenti
        viewModel.loadAddresses()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        item {
            Text("Delivery address", style = MaterialTheme.typography.headlineSmall)

            // Itera su tutti gli indirizzi esistenti
            addresses.forEach { address ->
                val isEditing = address == addressBeingEdited
                AddressRow(
                    address = address,
                    isSelected = address == selectedAddress,  // Verifica se l'indirizzo è quello selezionato
                    onSelect = { viewModel.selectAddress(address) },  // Seleziona l'indirizzo nel ViewModel
                    isEditing = isEditing,
                    onEditClick = {
                        if (isEditing) {
                            // Se l'indirizzo è in modifica, chiudi il form
                            viewModel.toggleEditAddress(null)  // Chiudi la modifica
                        } else {
                            viewModel.startEditAddress(address)  // Apri il form di modifica per questo indirizzo
                            viewModel.toggleEditAddress(address)  // Apri il form di modifica per questo indirizzo
                        }
                    },
                    onStateChange = { viewModel.onStateChange(it) },
                    onProvinceChange = { viewModel.onProvinceChange(it) },
                    onStreetChange = { viewModel.onStreetChange(it) },
                    onCityChange = { viewModel.onCityChange(it) },
                    onPostalCodeChange = { viewModel.onPostalCodeChange(it) },
                    onAdditionalInfoChange = { viewModel.onAdditionalInfoChange(it) },
                    street = street,
                    city = city,
                    postalCode = postalCode,
                    province = province,
                    state = state,
                    additionalInfo = additionalInfo

                )

            }


            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            AddAddressDropDown(
                isExpanded = isDropDownExpanded,
                onExpandChange = { viewModel.toggleDropDown() },  // Cambia lo stato quando cliccato
                firstName = (SessionManager.user?.firstName ?: ""),
                lastName = (SessionManager.user?.lastName ?: ""),
                _street = street,
                _province = province,
                _city = city,
                _postalCode = postalCode,
                _state = state,
                _additionalInfo = additionalInfo,
                onStateChange = { viewModel.onStateChange(it) },
                onProvinceChange = { viewModel.onProvinceChange(it) },
                onStreetChange = { viewModel.onStreetChange(it) },
                onCityChange = { viewModel.onCityChange(it) },
                onPostalCodeChange = { viewModel.onPostalCodeChange(it) },
                onAdditionalInfoChange = { viewModel.onAdditionalInfoChange(it) },
                isEditing = false
            )

            Spacer(modifier = Modifier.height(16.dp))
        }


        item {
            // Pulsante per salvare la selezione
            Button(
                onClick = { viewModel.onSaveClick(selectedAddress) },  // Torna indietro dopo aver selezionato
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}




@Composable
fun AddressRow(
    address: Address,
    isSelected: Boolean,
    onSelect: () -> Unit,
    isEditing: Boolean,
    onEditClick: () -> Unit, // Callback per aprire/chiudere la modifica
    onStateChange : (String) -> Unit,
    onProvinceChange: (String) -> Unit,
    onStreetChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPostalCodeChange: (String) -> Unit,
    onAdditionalInfoChange: (String) -> Unit,
    street : String,
    city : String,
    postalCode : String,
    province : String,
    state : String,
    additionalInfo : String


) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Riga per visualizzare l'indirizzo e il pulsante di modifica
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Selettore dell'indirizzo (RadioButton)
            RadioButton(
                selected = isSelected,
                onClick = { onSelect() }
            )

            // Visualizza l'indirizzo solo se non è in fase di modifica
            if (!isEditing)
            {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${address.street}, ${address.city}")
                    Text(address.postalCode)
                }
            }

            // Icona di modifica
            IconButton(onClick = { onEditClick() }) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                    contentDescription = if (isEditing) "Cancel edit" else "Edit address"
                )
            }
        }

        // Se stiamo modificando questo indirizzo, mostra il form di `AddAddressDropDown`
        if (isEditing) {
            Spacer(modifier = Modifier.height(8.dp))

            // Qui usiamo `AddAddressDropDown` per modificare l'indirizzo esistente
            SessionManager.user?.lastName?.let {
                SessionManager.user?.firstName?.let { it1 ->
                    AddAddressDropDown(
                        isExpanded = true,  // Sempre espanso durante la modifica
                        onExpandChange = { onEditClick() },  // Chiudi il form quando l'utente clicca
                        firstName = it1,
                        lastName = it,
                        _street = street,
                        onStreetChange = { onStreetChange(it) },
                        _city = city,
                        onCityChange = { onCityChange(it) },
                        _postalCode = postalCode,
                        onPostalCodeChange = { onPostalCodeChange(it) },
                        _province = province,
                        onProvinceChange = { onProvinceChange(it) },
                        _state = state,
                        onStateChange = { onStateChange(it) },
                        _additionalInfo = additionalInfo,
                        onAdditionalInfoChange = { onAdditionalInfoChange(it) },
                        isEditing = true
                    )
                }
            }
        }
    }
}




