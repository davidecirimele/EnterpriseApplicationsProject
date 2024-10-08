package com.example.ecommercefront_end.viewmodels

import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.BookFormat
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.BookLanguage
import com.example.ecommercefront_end.model.SaveAddress

import com.example.ecommercefront_end.model.UserId

import com.example.ecommercefront_end.repository.BookRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import retrofit2.Response
import java.time.LocalDate

class BookViewModel(private val repository: BookRepository): ViewModel() {

    private val _filteredProducts = MutableStateFlow<List<Book>>(emptyList())
    val filteredProducts: StateFlow<List<Book>> = _filteredProducts.asStateFlow()

    private val _minPrice = MutableStateFlow<Double?>(null)
    val minPrice: StateFlow<Double?> = _minPrice

    private val _maxPrice = MutableStateFlow<Double?>(null)
    val maxPrice: StateFlow<Double?> = _maxPrice

    private val _minAge = MutableStateFlow<Int?>(null)
    val minAge: StateFlow<Int?> = _minAge

    private val _maxAge = MutableStateFlow<Int?>(null)
    val maxAge: StateFlow<Int?> = _maxAge

    private val _minPages = MutableStateFlow<Int?>(null)
    val minPages: StateFlow<Int?> = _minPages

    private val _maxPages = MutableStateFlow<Int?>(null)
    val maxPages: StateFlow<Int?> = _maxPages

    private val _minWeight = MutableStateFlow<Double?>(null)
    val minWeight: StateFlow<Double?> = _minWeight

    private val _maxWeight = MutableStateFlow<Double?>(null)
    val maxWeight: StateFlow<Double?> = _maxWeight

    private val _startingPublicationYear = MutableStateFlow<LocalDate?>(null)
    val startingPublicationYear: StateFlow<LocalDate?> = _startingPublicationYear

    private val _filter = MutableStateFlow<BookFilter?>(null)
    val filter: StateFlow<BookFilter?> = _filter

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _isLoadingFilteredBooks = MutableStateFlow(true)
    val isLoadingFilteredBooks: StateFlow<Boolean> = _isLoadingFilteredBooks.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    fun fetchBooksData(){
        viewModelScope.launch {
            try {

                // Usa async per le chiamate parallele
                val priceJob = async { fetchPrice() }
                val ageJob = async { fetchAge() }
                val pagesJob = async { fetchPages() }
                val weightJob = async { fetchWeight() }
                val startingYearJob = async { fetchStartingYear() }

                priceJob.await()
                ageJob.await()
                pagesJob.await()
                weightJob.await()
                startingYearJob.await()

            } catch (e: Exception) {
                Log.e("Book Error", "Error loading books data", e)
            }finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchPrice(){

            try {
                val response1 = repository.getMinPrice()
                val response2 = repository.getMaxPrice()

                if (response1.isSuccessful && response1.body() != null) {
                    _minPrice.value = response1.body()
                }else {
                    throw Exception("Failed to fetch min price")
                }
                if (response2.isSuccessful && response2.body() != null){
                    _maxPrice.value = response2.body()
                    Log.d("BookDebug", "minPrice: ${_minPrice.value}, maxPrice: ${_maxPrice.value}")
                } else {
                    throw Exception("Failed to fetch max price")
                }
            } catch (e: Exception) {
                Log.d("BookDebug", "minPrice: ${_minPrice.value}, maxPrice: ${_maxPrice.value}")
            }
    }

    private suspend fun fetchAge(){

            try {
                val response1 = repository.getMinAge()
                val response2 = repository.getMaxAge()

                if (response1.isSuccessful && response1.body() != null) {
                    _minAge.value = response1.body()
                }else {
                    throw Exception("Failed to fetch min age")
                }
                if (response2.isSuccessful && response2.body() != null){
                    _maxAge.value = response2.body()
                } else {
                    throw Exception("Failed to fetch max age")
                }
            } catch (e: Exception) {
                Log.d("BookDebug", "minAge: ${_minAge.value}, maxAge: ${_maxAge.value}")
            }
    }

    private suspend fun fetchPages(){
            try {
                val response1 = repository.getMinPages()
                val response2 = repository.getMaxPages()

                if (response1.isSuccessful && response1.body() != null) {
                    _minPages.value = response1.body()
                }else {
                    throw Exception("Failed to fetch min pages")
                }
                if (response2.isSuccessful && response2.body() != null){
                    _maxPages.value = response2.body()
                } else {
                    throw Exception("Failed to fetch max pages")
                }
            } catch (e: Exception) {
                Log.d("BookDebug", "minPages: ${_minPages.value}, maxPages: ${_maxPages.value}")
            }
    }

    private suspend fun fetchWeight(){

        try {
            val response1 = repository.getMinWeight()
            val response2 = repository.getMaxWeight()

            if (response1.isSuccessful && response1.body() != null) {
                _minWeight.value = response1.body()
            }else {
                throw Exception("Failed to fetch min weight")
            }
            if (response2.isSuccessful && response2.body() != null){
                _maxWeight.value = response2.body()
                Log.d("BookDebug", "minWeight: ${_minWeight.value}, maxWeight: ${_maxWeight.value}")
            } else {
                throw Exception("Failed to fetch max weight")
            }
        } catch (e: Exception) {
            Log.d("BookDebug", "minWeight: ${_minWeight.value}, maxWeight: ${_maxWeight.value}")
        }
    }

    private suspend fun fetchStartingYear(){

        try {
            val response = repository.getMinPublicationYear()

            if (response.isSuccessful && response.body() != null) {
                _startingPublicationYear.value = response.body()
            }else {
                throw Exception("Failed to fetch startingPublicationYear")
            }
        } catch (e: Exception) {
            Log.d("BookDebug", "startingPublicationYear: ${_startingPublicationYear.value}")
        }
    }

    fun fetchFilteredBooks() {
        viewModelScope.launch {
            try {
                if(filter.value != null) {

                    var response = repository.getFilteredBooks(filter.value!!)

                    if (response.isSuccessful && response.body() != null) {
                        _filteredProducts.value = response.body()!!
                    } else {
                        throw Exception("Error fetching products")
                    }
                }else {
                    throw Exception("Error with filter")
                }
            } catch (e: Exception) {
                _error.value = "Errore durante il caricamento dei libri: ${e.message}" // Imposta il messaggio di errore
            } finally {
                _isLoadingFilteredBooks.value = false
            }
        }
    }

    fun updateFilter(weight : Double? = null, minPrice : Double? = null, maxPrice : Double? = null, stock : Int? = null, title : String? = null, author : String? = null,
                             ISBN : String? = null, minPages : Int? = null, maxPages : Int? = null, edition : String? = null, format : BookFormat? = null, genre : BookGenre? = null,
                             language : BookLanguage? = null, publisher : String? = null, minAge : Int? = null, maxAge : Int? = null, minPublishDate : LocalDate? = null,
                             maxPublishDate : LocalDate? = null){

        val currentFilter = _filter.value ?: BookFilter()
        _filter.value = currentFilter.copy(
            weight = weight ?: currentFilter.weight,
            minPrice = minPrice ?: currentFilter.minPrice,
            maxPrice = maxPrice ?: currentFilter.maxPrice,
            stock = stock ?: currentFilter.stock,
            title = title ?: currentFilter.title,
            author = author ?: currentFilter.author,
            ISBN = ISBN ?: currentFilter.ISBN,
            edition = edition ?: currentFilter.edition,
            minPages = minPages ?: currentFilter.minPages,
            maxPages = maxPages ?: currentFilter.minPages,
            genre = genre ?: currentFilter.genre,
            format = format ?: currentFilter.format,
            language = language ?: currentFilter.language,
            publisher = publisher ?: currentFilter.publisher,
            minAge = minAge ?: currentFilter.minAge,
            maxAge = maxAge ?: currentFilter.maxAge,
            minPublishDate = minPublishDate ?: currentFilter.minPublishDate,
            maxPublishDate = maxPublishDate ?: currentFilter.maxPublishDate)
    }

    fun resetFilter(){
        _filter.value = BookFilter()
    }
}