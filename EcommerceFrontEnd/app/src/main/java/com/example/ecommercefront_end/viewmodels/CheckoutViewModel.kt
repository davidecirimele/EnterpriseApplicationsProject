import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.CardProvider
import com.example.ecommercefront_end.model.CheckoutRequest
import com.example.ecommercefront_end.model.PaymentMethod
import com.example.ecommercefront_end.model.PaymentMethodId
import com.example.ecommercefront_end.model.PaymentMethodType
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveOrder
import com.example.ecommercefront_end.model.SavePaymentMethod
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.repository.AddressRepository
import com.example.ecommercefront_end.repository.CheckoutRepository
import com.example.ecommercefront_end.utils.ErrorMessageParser
import com.example.ecommercefront_end.viewmodels.CartViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.json.JSONObject
import java.net.SocketTimeoutException


class CheckoutViewModel(private val checkoutRepository: CheckoutRepository, private val cartViewModel: CartViewModel, private val navController: NavController) : ViewModel() {

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

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods

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

    private val _cardHolderName = MutableStateFlow("")
    val cardHolderName: StateFlow<String> = _cardHolderName

    private val _cardNumber = MutableStateFlow("")
    val cardNumber: StateFlow<String> = _cardNumber

    private val _expirationDate = MutableStateFlow("")
    val expirationDate: StateFlow<String> = _expirationDate

    private val _paymentMethodType = MutableStateFlow<PaymentMethodType?>(null)
    val paymentMethodType: StateFlow<PaymentMethodType?> = _paymentMethodType

    private val _selectedCardProvider = MutableStateFlow<CardProvider?>(null)
    val selectedCardProvider: StateFlow<CardProvider?> = _selectedCardProvider

    private val _isAddingNewPaymentMethod = MutableStateFlow(false)
    val isAddingNewPaymentMethod: StateFlow<Boolean> = _isAddingNewPaymentMethod

    private val _selectedPaymentMethodType = MutableStateFlow<PaymentMethodType?>(null)
    val selectedPaymentMethodType: StateFlow<PaymentMethodType?> = _selectedPaymentMethodType

    private val _order = MutableStateFlow<SaveOrder?>(null)
    val order: StateFlow<SaveOrder?> = _order

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val snackbarHostState = SnackbarHostState()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    val isCheckoutEnabled: StateFlow<Boolean> = combine(
        _selectedAddress,
        _selectedPaymentMethod
    ) { address, paymentMethod ->
        address != null && paymentMethod != null  // Il pulsante è abilitato solo se entrambi sono selezionati
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)


    val isNewAddressIsSaved: StateFlow<Boolean> = combine(
        _street,
        _province,
        _city,
        _postalCode,
        _state
    ) { street, province, city, postalCode, state -> println("street: $street, province: $province, city: $city, postalCode: $postalCode, state: $state")
        street.isNotBlank() && province.isNotBlank() && city.isNotBlank() && postalCode.isNotBlank() && state.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        observeUserChanges()
    }

    private fun observeUserChanges() {
        viewModelScope.launch {
            SessionManager.observableUser.collectLatest { user ->
                if (user != null) {
                    clearData()
                }
            }
        }
    }


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

    fun prepareNewAdddress() {
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

    fun onSaveClick(address: Address?) {
        println("è da salvare un nuovo indirizzo?" + isNewAddressIsSaved.value)
        println("viewModel data: ${_street.value}, ${_province.value}, ${_city.value}, ${_postalCode.value}, ${_state.value}")
        println("textfields data: ${street.value}, ${province.value}, ${city.value}, ${postalCode.value}, ${state.value}")
        if (address != null && _addressBeingEdited.value == address) {
            onSaveEdit(address)
        } else if (isNewAddressIsSaved.value) {
            onSave()
        }
    }

    fun addAndSelectNewAddress(newAddress: SaveAddress, sessionManager: SessionManager) {
        viewModelScope.launch {
            try {
                _addresses.value = _addresses.value.toMutableList().apply {
                    checkoutRepository.addShippingAddress(newAddress, sessionManager)
                        ?.let { add(it) }
                }
                _selectedAddress.value = _addresses.value.last()
            } catch (e: Exception) {
                // Gestire l'errore


            }
        }
    }

    fun toggleDropDown() {
        _isDropDownExpanded.value = !_isDropDownExpanded.value
        prepareNewAdddress()
    }

    fun onStreetChange(street: String) {
        this._street.value = street

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

    fun onSaveEdit(address: Address) {
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
            try {
                checkoutRepository.updateShippingAddress(newAddress, SessionManager, addressId)
                _selectedAddress.value = address
                toggleDropDown()
                prepareNewAdddress()
            }
            catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while saving the address: Connection problem."
                else
                    _errorMessage.value = "Error while saving the address."
            }
        }
    }

    // Funzione per selezionare il metodo di pagamento
    fun selectPaymentMethod(paymentMethod: PaymentMethod) {
        _selectedPaymentMethod.value = paymentMethod

    }

    fun onAddPaymentMethodClick(onSuccess: ()->Unit) {
        viewModelScope.launch {

            val user = SessionManager.getUser()
            val newPaymentMethod = SavePaymentMethod(
                user = user,
                cardHolderName = _cardHolderName.value,
                cardNumber = _cardNumber.value.filter { it.isDigit() },
                paymentMethodType = _selectedPaymentMethodType.value,
                provider = _selectedCardProvider.value,
                expirationDate = _expirationDate.value
            )

            try {
                val addResponse = checkoutRepository.addPaymentMethod(newPaymentMethod)

                if(addResponse.isSuccessful) {
                    val paymentMethod = addResponse.body()
                    if (paymentMethod != null) {
                        resetPaymentMethodForm()

                        val getResponse =
                            checkoutRepository.getPaymentMethod(user.userId, paymentMethod.id)
                        if (getResponse.isSuccessful) {
                            getResponse.body()?.let { pm ->
                                _paymentMethods.value =
                                    _paymentMethods.value.toMutableList().apply {
                                        add(pm)
                                    }
                                _selectedPaymentMethod.value = pm
                                onSuccess()
                            }
                        } else {
                            val errorBody = getResponse.errorBody()?.string()

                            _errorMessage.value = ErrorMessageParser(errorBody)
                        }
                    }
                }else {
                    val errorBody = addResponse.errorBody()?.string()

                    _errorMessage.value = ErrorMessageParser(errorBody)
                }
            }
            catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while saving the payment method: Connection problem."
                else
                    _errorMessage.value = "Error while saving the payment method."
            }
        }

    }


    fun toggleAddNewPaymentMethod() {
        _isAddingNewPaymentMethod.value = !_isAddingNewPaymentMethod.value
    }

    fun onCardNumberChange(cardNumber: String) {
        _cardNumber.value = cardNumber
    }

    fun onExpirationDateChange(expirationDate: String) {
        _expirationDate.value = expirationDate
    }

    fun onCardHolderNameChange(cardHolderName: String) {
        _cardHolderName.value = cardHolderName
    }

    fun selectCardProvider(cardProvider: CardProvider?) {
        _selectedCardProvider.value = cardProvider
    }

    fun resetPaymentMethodForm() {
        _cardHolderName.value = ""
        _cardNumber.value = ""
        _expirationDate.value = ""
    }

    fun selectPaymentMethodType(paymentMethodType: PaymentMethodType) {
        _selectedPaymentMethodType.value = paymentMethodType
    }


    fun deletePaymentMethod(paymentMethod: Long) {
        viewModelScope.launch {
            val user = SessionManager.user?.id
            if (user != null)
                try {
                    checkoutRepository.deletePaymentMethod(user, paymentMethod)
                    _paymentMethods.value = _paymentMethods.value.toMutableList().apply {
                        removeIf { it.id == paymentMethod }
                    }

                    if (_selectedPaymentMethod.value?.id == paymentMethod) {
                        _selectedPaymentMethod.value = _paymentMethods.value.firstOrNull()

                    }
                } catch (e: Exception) {
                    if (e is SocketTimeoutException)
                        _errorMessage.value =
                            "Error while deleting the payment method: Connection problem."
                    else
                        _errorMessage.value = "Error while deleting the payment method."
                }
        }
    }


    fun confirmOrder() {
        if (isCheckoutEnabled.value) {
            viewModelScope.launch {
                val user = SessionManager.getUser()
                val pMId = _selectedPaymentMethod.value?.id
                val add = _selectedAddress.value
                if (pMId != null && add != null) {
                    val checkoutRequest = CheckoutRequest(
                        userId = user,
                        paymentMethodId = PaymentMethodId(pMId),
                        address = add
                    )
                    println("checkoutRequest: $checkoutRequest")
                    try {
                        val order = checkoutRepository.confirmOrder(checkoutRequest)
                        _order.value = order.body()
                        navController.navigate("order-confirmation")
                    }
                    catch (e: Exception) {
                        if (e is SocketTimeoutException)
                            _errorMessage.value = "Error while confirming the order: Connection problem."
                        else
                            _errorMessage.value = "Error while confirming the order."
                    }

                }
            }
        }
    }

    fun loadCheckoutData() {
        viewModelScope.launch {
            try {
                val user = SessionManager.user?.id
                if (user != null) {
                    if (_selectedAddress.value == null) {
                        val response =
                            checkoutRepository.getShippingAddress(user)
                        if (response.isSuccessful) {
                            val address =
                                response.body()
                            if (address != null) {
                                _selectedAddress.value = address
                            }
                        } else {
                            _selectedAddress.value = null
                        }
                    }

                    if (_selectedPaymentMethod.value == null) {
                        val response = checkoutRepository.getPaymentMethods(user)

                        if (response.isSuccessful) {
                            val paymentMethods = response.body()

                            if (paymentMethods != null) {
                                _paymentMethods.value = paymentMethods
                                _selectedPaymentMethod.value = paymentMethods.firstOrNull()
                            }
                        }
                    }
                    _totalAmount.value = cartViewModel.totalAmount.value

                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while loading the checkout data: Connection problem."
                else
                    _errorMessage.value = "Error while loading the checkout data."
            } finally {
                _isLoading.value = false
                _isRefreshing.value = false
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
                _errorMessage.value = "Error while loading the addresses."
            }
        }
    }

    fun loadPaymentMethods() {
        viewModelScope.launch {
            try {
                val user = SessionManager.user?.id
                if (user != null) {
                    val response = checkoutRepository.getPaymentMethods(user)
                    if (response.isSuccessful) {
                        val paymentMethods = response.body()
                        if (paymentMethods != null) {
                            _paymentMethods.value = paymentMethods
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error while loading the payment methods."
            }
        }
    }

    fun clearData() {
        _selectedAddress.value = null
        _selectedPaymentMethod.value = null
        _addresses.value = emptyList()
    }

    fun onSnackbarDismissed(){
        _errorMessage.value = null
    }

    fun onLogout(){
        //viewModelScope.cancel()
        clearData()
    }

}