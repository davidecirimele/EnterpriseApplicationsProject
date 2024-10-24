package com.example.ecommercefront_end.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.BookFormat
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.BookLanguage
import com.example.ecommercefront_end.model.Price
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.model.Stock

import com.example.ecommercefront_end.repository.BookRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class BookViewModel(private val repository: BookRepository): ViewModel() {

    private val _filteredProducts = MutableStateFlow<List<Book>>(emptyList())
    val filteredProducts: StateFlow<List<Book>> = _filteredProducts.asStateFlow()

    private val _cachedProducts = MutableStateFlow<List<Book>>(emptyList())
    val cachedProducts: StateFlow<List<Book>> = _cachedProducts.asStateFlow()

    private val _allProducts = MutableStateFlow<List<Book>>(emptyList())
    val allProducts: StateFlow<List<Book>> = _allProducts.asStateFlow()

    private val _allAvailableProducts = MutableStateFlow<List<Book>>(emptyList())
    val allAvailableProducts: StateFlow<List<Book>> = _allAvailableProducts.asStateFlow()

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

    private val _filter = MutableStateFlow<BookFilter>(BookFilter())
    val filter: StateFlow<BookFilter> = _filter

    private val _sortOption = MutableStateFlow("Newest")
    val sortOption: StateFlow<String> = _sortOption

    private val _bookFlow = MutableStateFlow<Book?>(null)
    val bookFlow: StateFlow<Book?> = _bookFlow.asStateFlow()

    private val _isLoadingAllBooks = MutableStateFlow(true)
    val isLoadingAllBooks: StateFlow<Boolean> = _isLoadingAllBooks.asStateFlow()

    private val _isLoadingBook = MutableStateFlow(true)
    val isLoadingBook: StateFlow<Boolean> = _isLoadingBook.asStateFlow()

    private val _isLoadingCatalogue = MutableStateFlow(true)
    val isLoadingCatalogue: StateFlow<Boolean> = _isLoadingCatalogue.asStateFlow()

    private val _isLoadingData = MutableStateFlow(true)
    val isLoadingData: StateFlow<Boolean> = _isLoadingData.asStateFlow()

    private val _isLoadingFilteredBooks = MutableStateFlow(true)
    val isLoadingFilteredBooks: StateFlow<Boolean> = _isLoadingFilteredBooks.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        resetFilter()
        viewModelScope.launch {
            fetchAllAvailableProducts()
        }
        fetchBooksData()
    }

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
                _isLoadingData.value = false
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

    private suspend fun fetchAllProducts() {
        try {
            val response = repository.getAllBooks()

            if (response.isSuccessful && response.body() != null) {
                _allProducts.value = response.body()!!
                _filteredProducts.value = _allProducts.value
            } else {
                throw Exception("Error fetching products")
            }

        } catch (e: Exception) {
            _error.value = "Errore durante il caricamento dei libri: ${e.message}" // Imposta il messaggio di errore
        } finally {
            _isLoadingAllBooks.value = false
        }
    }

    suspend fun fetchAllAvailableProducts() {
        try {
            val response = repository.getCatalogue()

            if (response.isSuccessful && response.body() != null) {
                _allAvailableProducts.value = response.body()!!
                _filteredProducts.value = _allAvailableProducts.value
            } else {
                throw Exception("Error fetching products")
            }

        } catch (e: Exception) {
            _error.value = "Errore durante il caricamento dei libri: ${e.message}" // Imposta il messaggio di errore
        } finally {
            _isLoadingCatalogue.value = false
        }
    }

    suspend fun fetchFilteredBooks() {

            try {
                val response = repository.getFilteredBooks(filter.value)

                if (response.isSuccessful && response.body() != null) {
                    _filteredProducts.value = response.body()!!
                    _cachedProducts.value = filteredProducts.value
                } else {
                    throw Exception("Error fetching products")
                }

            } catch (e: Exception) {
                _error.value = "Errore durante il caricamento dei libri: ${e.message}" // Imposta il messaggio di errore
            } finally {
                _isLoadingFilteredBooks.value = false
            }

    }

    fun updateFilter(weight : Double? = null, minPrice : Double? = null, maxPrice : Double? = null, stock : Int? = null, title : String? = null, author : String? = null,
                             ISBN : String? = null, minPages : Int? = null, maxPages : Int? = null, edition : String? = null, format : BookFormat? = null, genre : BookGenre? = null,
                             language : BookLanguage? = null, publisher : String? = null, minAge : Int? = null, maxAge : Int? = null, minPublishDate : LocalDate? = null,
                             maxPublishDate : LocalDate? = null, available: Boolean? = true){

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
            maxPublishDate = maxPublishDate ?: currentFilter.maxPublishDate,
            available = available ?: currentFilter.available)
    }

    fun resetFilter(){
        _filter.value = BookFilter()
        clearCache()
    }

    fun setOrderOption(option: String){
        _sortOption.value = option
        sortProducts()
    }

    fun searchBooks(navController: NavController, currentRoute: String?){
        if(currentRoute != null && (currentRoute != "filtered-books" && currentRoute != "admin-catalogue")) {
            navController.navigate("filtered-books") {
                popUpTo("home") {
                    saveState = true
                }
            }
        }

        viewModelScope.launch {
            fetchFilteredBooks()
        }


        /*
        if(!searchInCachedBooks()) {
            Log.d("BookDebug", "Too few cached products found, fetching from backend...")
            fetchFilteredBooks()
        }*/
    }

    fun sortProducts(){
        if(filteredProducts.value.isNotEmpty()){
            _filteredProducts.value = when (_sortOption.value) {
                "Price: Low to High" -> _filteredProducts.value.sortedBy { it.price }
                "Price: High to Low" -> _filteredProducts.value.sortedByDescending { it.price }
                "Weight: Low to High" -> filteredProducts.value.sortedBy { it.weight }
                "Weight: High to Low" -> filteredProducts.value.sortedByDescending { it.weight }
                "Number of pages: Low to High" -> filteredProducts.value.sortedBy { it.pages }
                "Number of pages: High to Low" -> filteredProducts.value.sortedByDescending { it.pages }
                "Age: Low to High" -> filteredProducts.value.sortedBy { it.age }
                "Age: High to Low" -> filteredProducts.value.sortedByDescending { it.age }
                "Newest" -> _filteredProducts.value.sortedByDescending { it.publishDate.year}
                "Oldest" -> _filteredProducts.value.sortedBy { it.publishDate.year}
                else -> _filteredProducts.value
            }
        }
    }

    private fun searchInCachedBooks(): Boolean {
        if(cachedProducts.value.isNotEmpty()){
            Log.d("BookDebug", "Cached products found, applying filters...")
            _filteredProducts.value = cachedProducts.value.filter { book ->
                (filter.value.weight == null || book.weight == filter.value.weight) &&
                        (filter.value.minPrice == null || book.price >= filter.value.minPrice!!) &&
                        (filter.value.maxPrice == null || book.price <= filter.value.maxPrice!!) &&
                        (filter.value.stock == null || book.stock >= filter.value.stock!!) &&
                        ((filter.value.title == null || book.title.contains(filter.value.title!!, ignoreCase = true)) ||
                        (filter.value.author == null || book.author.contains(filter.value.author!!, ignoreCase = true)) ||
                                (filter.value.publisher == null || book.publisher.contains(filter.value.publisher!!, ignoreCase = true))) &&
                        (filter.value.ISBN == null || book.ISBN.contains(filter.value.ISBN!!, ignoreCase = true)) &&
                        (filter.value.minPages == null || book.pages >= filter.value.minPages!!) &&
                        (filter.value.maxPages == null || book.pages <= filter.value.maxPages!!) &&
                        (filter.value.edition == null || book.edition.equals(filter.value.edition, ignoreCase = true)) &&
                        (filter.value.format == null || book.format == filter.value.format) &&
                        (filter.value.genre == null || book.genre == filter.value.genre) &&
                        (filter.value.language == null || book.language == filter.value.language) &&
                        (filter.value.minAge == null || book.age >= filter.value.minAge!!) &&
                        (filter.value.maxAge == null || book.age <= filter.value.maxAge!!) &&
                        (filter.value.minPublishDate == null || !book.publishDate.isBefore(filter.value.minPublishDate)) &&
                        (filter.value.maxPublishDate == null || !book.publishDate.isAfter(filter.value.maxPublishDate))
            }
            Log.d("BookDebug", "Filter values: ${filter.value}")
            Log.d("BookDebug", "Filtered products: ${_filteredProducts.value.size}")
        }
        return _filteredProducts.value.isNotEmpty()
    }

    fun clearCache(){
        _cachedProducts.value = emptyList<Book>()
    }


    suspend fun loadBook(id: Long) {
        try {
            val response = repository.getBook(id)

            if (response.isSuccessful && response.body() != null) {
                _bookFlow.value = response.body()
            } else {
                _bookFlow.value = null
                throw Exception("Error fetching book with id $id")
            }

        } catch (e: Exception) {
            _error.value = "Errore durante il caricamento del libro: ${e.message}" // Imposta il messaggio di errore
        } finally {
            _isLoadingBook.value = false
        }
    }

    fun insertBook(book: SaveBook){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    repository.insertBook(book)
                    fetchAllAvailableProducts()
                    Log.d("BookViewModel", "Book added: ${_allAvailableProducts.value}")
                }
                else
                    Log.d("UserDebug", "User is null")
            } catch (e: Exception) {
                Log.d("UserDebug", "Error adding Book: $book")
            }
        }
    }

    fun updatePrice(newPrice: Double, bookId: Long){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    Log.d("UserDebug", "NewPrice ${Price(newPrice)}")
                    repository.updatePrice(bookId, Price(newPrice))
                }
                else
                    Log.d("UserDebug", "User is null")
            } catch (e: Exception) {
                e.message?.let { Log.d("UserDebug", it) }
            }
        }
    }

    fun restock(newStock: Int, bookId: Long){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    Log.d("UserDebug", "NewStock $newStock")
                    repository.updateStock(bookId, Stock(newStock))
                }
                else
                    Log.d("UserDebug", "User is null")
            } catch (e: Exception) {
                Log.d("UserDebug", "Error updating Book stock")
            }
        }
    }


    fun removeBook(bookId: Long){
        viewModelScope.launch {
            try{
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    repository.removeBook(bookId)

                    _allProducts.value = emptyList()
                    fetchAllAvailableProducts()
                }
                else{
                    throw Exception("Access Denied")
                }
            } catch (e: Exception) {
                Log.d("UserDebug", "Error deleting Book with id $bookId")
            }
        }
    }

    fun restoreBook(bookId: Long){
        viewModelScope.launch {
            try{
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    repository.restoreBook(bookId)

                    _allProducts.value = emptyList()
                    fetchAllAvailableProducts()
                }
                else{
                    throw Exception("Access Denied")
                }
            } catch (e: Exception) {
                Log.d("UserDebug", "Error deleting Book with id $bookId")
            }
        }
    }
}