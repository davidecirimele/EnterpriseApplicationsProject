package com.example.ecommercefront_end.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<Book>>(emptyList())
    val products: StateFlow<List<Book>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadBooks() // Avvia il caricamento dei libri non appena il ViewModel viene creato
        println("ViewModel inizializzato")
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true // Imposta isLoading a true prima del caricamento
            try {
                _products.value = repository.getAllBooks() // Aggiorna _products con i libri caricati
                if (_products.value.isEmpty()) {
                    _error.value = "Nessun libro trovato" // Imposta un messaggio di errore se non ci sono libri
                }
                else {
                    _error.value = "libri presi" // Azzera il messaggio di errore se ci sono libri
                }
            } catch (e: Exception) {
                _error.value = "Errore durante il caricamento dei libri: ${e.message}" // Imposta il messaggio di errore
            } finally {
                _isLoading.value = false // Imposta isLoading a false dopo il caricamento, indipendentemente dal risultato
            }
        }
    }

    /*
    private fun loadBooks() {
        viewModelScope.launch {
            try {
                val books = repository.getAllBooks()
            } catch (e: Exception) {
                // Gestire l'errore, ad esempio mostrando un messaggio all'utente
            }
        }
    }*/
}


