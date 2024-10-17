import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.repository.CheckoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CheckoutViewModel(private val checkoutRepository: CheckoutRepository) : ViewModel() {


    private val _street = MutableStateFlow("")
    val street: StateFlow<String> = _street

    private val _province = MutableStateFlow("")
    val province: StateFlow<String> = _province

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _postalCode = MutableStateFlow("")
    val postalCode: StateFlow<String> = _postalCode

    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state

    private val _additionalInfo = MutableStateFlow("")
    val additionalInfo: StateFlow<String> = _additionalInfo



    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses

    // Stato per l'indirizzo di spedizione selezionato
    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress

    // Stato per il metodo di pagamento selezionato
    private val _selectedPaymentMethod = MutableStateFlow<PaymentMethod?>(null)
    val selectedPaymentMethod: StateFlow<PaymentMethod?> = _selectedPaymentMethod

    // Stato per il totale dell'ordine
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    // Stato per il form di aggiunta indirizzo
    private val _isDropDownExpanded = MutableStateFlow(false)
    val isDropDownExpanded: StateFlow<Boolean> = _isDropDownExpanded

    private val _addressBeingEdited = MutableStateFlow<Address?>(null)
    val addressBeingEdited: StateFlow<Address?> = _addressBeingEdited

    // Funzione per attivare o disattivare la modalità di modifica di un indirizzo
    fun toggleEditAddress(address: Address?) {
        if (_addressBeingEdited.value == address) {
            _addressBeingEdited.value = null  // Se è lo stesso indirizzo, chiudi la modifica
        } else {
            _addressBeingEdited.value = address  // Apri la modifica per questo indirizzo
        }
    }


    // Funzione per selezionare l'indirizzo
    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }

    fun prepareNewAdddress(){
        _addressBeingEdited.value = null
        _street.value = ""
        _province.value = ""
        _city.value = ""
        _postalCode.value = ""
        _state.value = ""
        _additionalInfo.value = ""
    }

    fun startEditAddress(address: Address) {
        _street.value = address.street
        _province.value = address.province
        _city.value = address.city
        _postalCode.value = address.postalCode
        _state.value = address.state
        _additionalInfo.value = address.additionalInfo
    }

    fun onSaveClick(address: Address?){
        if (address != null && _addressBeingEdited.value != null) {
            println("indirizzo da salvare: $address")
            onSaveEdit(address)
        } else {
            onSave()
        }
    }




    fun  addAndSelectNewAddress(newAddress: SaveAddress, sessionManager: SessionManager) {
        viewModelScope.launch {
            _addresses.value = _addresses.value.toMutableList().apply {
                checkoutRepository.addShippingAddress(newAddress, sessionManager)
                    ?.let { add(it) }
            }
            _selectedAddress.value = _addresses.value.last()
            println("indirizzo selezionato: ${_selectedAddress.value}")

        }
    }

    fun toggleDropDown() {
        _isDropDownExpanded.value = !_isDropDownExpanded.value
        prepareNewAdddress()
    }

    fun onStreetChange(street: String) {
            this._street.value = street
        println("street immessa: $street")
        print("street nel viewmodel: ${_street.value}")
    }

    fun onCityChange(city: String) {
            this._city.value = city
    }

    fun onPostalCodeChange(postalCode: String) {
            this._postalCode.value = postalCode
    }

    fun onAdditionalInfoChange(additionalInfo: String) {
            this._additionalInfo.value = additionalInfo

    }

    fun onStateChange(state: String) {
            this._state.value = state
    }

    fun onProvinceChange(province: String) {
            this._province.value = province
    }

    fun onSave() {
        viewModelScope.launch {
            val newAddress = SaveAddress(
                street = _street.value,
                province = _province.value,
                city = _city.value,
                state = _state.value,
                postalCode = _postalCode.value,
                additionalInfo = _additionalInfo.value
            )
            addAndSelectNewAddress(newAddress, SessionManager)
            toggleDropDown()
            prepareNewAdddress()
        }
    }

    fun onSaveEdit( address: Address){
        viewModelScope.launch {
            val addressId = address.id
            val newAddress = SaveAddress(
                street = address.street,
                province = address.province,
                city = address.city,
                state = address.state,
                postalCode = address.postalCode,
                additionalInfo = address.additionalInfo
            )
            checkoutRepository.updateShippingAddress(newAddress, SessionManager, addressId)
            _selectedAddress.value = address
            toggleDropDown()
            prepareNewAdddress()
        }
    }



    // Funzione per selezionare il metodo di pagamento
    fun selectPaymentMethod(paymentMethod: PaymentMethod) {
        _selectedPaymentMethod.value = paymentMethod
    }

    // Funzione per aggiornare il totale
    fun updateTotalAmount(total: Double) {
        _totalAmount.value = total
    }

    // Funzione per confermare l'ordine (placeholder)
    fun confirmOrder(): Boolean {
        return _selectedAddress.value != null && _selectedPaymentMethod.value != null
    }

    fun loadCheckoutData() {
        viewModelScope.launch {
            try {
                val user = SessionManager.user?.id
                if (user != null) {
                    if (_selectedAddress.value == null) {
                        val response =
                            checkoutRepository.getShippingAddress(user) // Chiamata alla funzione suspend
                        if (response.isSuccessful) {  // Controlla se la richiesta è andata a buon fine
                            val address =
                                response.body()  // Ottieni il corpo della risposta (Address?)
                            if (address != null) {
                                _selectedAddress.value = address  // Imposta l'indirizzo selezionato
                            }
                        } else {
                            _selectedAddress.value = null
                        }
                    }


                    _totalAmount.value = checkoutRepository.getOrderTotal(user) // Ottieni il totale dell'ordine
                }
            } catch (e: Exception) {
                // Gestire l'errore
            }
        }
    }

    fun loadAddresses() {
        viewModelScope.launch {
            try {
                val user = SessionManager.user?.id
                if (user != null) {
                    val response = checkoutRepository.getShippingAddresses(user)
                    if (response.isSuccessful) {
                        val addresses = response.body()
                        if (addresses != null) {
                            _addresses.value = addresses
                        }
                    }
                }
            } catch (e: Exception) {
                // Gestire l'errore
            }
        }
    }

}
