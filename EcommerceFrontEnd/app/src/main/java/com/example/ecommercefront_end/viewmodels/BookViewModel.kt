package com.example.ecommercefront_end.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bumptech.glide.util.LruCache
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.Price
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.model.Stock

import com.example.ecommercefront_end.repository.BookRepository
import com.example.ecommercefront_end.utils.ErrorMessageParser
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.net.SocketTimeoutException
import java.time.LocalDate
import kotlin.math.log

class BookViewModel(private val repository: BookRepository): ViewModel() {

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

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8

    private val imageCache = LruCache<String, Bitmap>(cacheSize.toLong())

    private val bookCache = LruCache<Long, Book>(cacheSize.toLong())

    private val _halloweenBooks = MutableStateFlow<List<Book>>(emptyList())
    val halloweenBooks: StateFlow<List<Book>> = _halloweenBooks

    private val _recentBooks = MutableStateFlow<List<Book>>(emptyList())
    val recentBooks: StateFlow<List<Book>> = _recentBooks

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val snackbarHostState = SnackbarHostState()

    init {
        viewModelScope.launch {
            fetchAllAvailableProducts()
        }
        fetchBooksData()
    }

    fun fetchBooksData(){
        _isLoadingData.value = true
        viewModelScope.launch {
            try {

                // Usa async per le chiamate parallele
                val priceJob = async { fetchValues(repository.getMinPrice(), repository.getMaxPrice(), _minPrice,_maxPrice) }
                val ageJob = async { fetchValues(repository.getMinAge(), repository.getMaxAge(), _minAge,_maxAge) }
                val pagesJob = async { fetchValues(repository.getMinPages(), repository.getMaxPages(), _minPages,_maxPages) }
                val weightJob = async { fetchValues(repository.getMinWeight(), repository.getMaxWeight(), _minWeight,_maxWeight) }
                val startingYearJob = async { fetchValues(fetchMin = repository.getMinPublicationYear(), minState = _startingPublicationYear) }

                priceJob.await()
                ageJob.await()
                pagesJob.await()
                weightJob.await()
                startingYearJob.await()

            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while fetching books data: Connection problem."
                else
                    _errorMessage.value = "Error while fetching books data."
            }finally {
                _isLoadingData.value = false
            }
        }
    }

    private suspend fun <T> fetchValues(
        fetchMin: Response<T>,
        fetchMax: Response<T>? =  null,
        minState: MutableStateFlow<T?>,
        maxState: MutableStateFlow<T?>? = null
    ) {
        if((minState.value == null && maxState == null) || (minState.value == null && (maxState != null && maxState.value == null)))
            try {
                if (fetchMin.isSuccessful && fetchMin.body() != null) {
                    minState.value = fetchMin.body()
                } else {
                     _errorMessage.value = "Failed to fetch min value"
                }

                if (fetchMax != null) {
                    if (fetchMax.isSuccessful && fetchMax.body() != null) {
                        if (maxState != null) {
                            maxState.value = fetchMax.body()
                        }
                        else{
                            _errorMessage.value = "Failed to fetch max value"
                        }
                    } else {
                        _errorMessage.value = "Failed to fetch max value"
                    }
                }

            } catch (e: Exception) {
                throw e
            }
    }

    private suspend fun fetchAllProducts() {
        _isLoadingAllBooks.value = true
        try {
            val response = repository.getAllBooks()

            if (response.isSuccessful && response.body() != null) {
                _allProducts.value = response.body()!!
            } else {
                throw Exception("Error fetching products")
            }

        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching all products: Connection problem."
            else
                _errorMessage.value = "Error while fetching all products."
        } finally {
            _isLoadingAllBooks.value = false
        }
    }

    suspend fun fetchAllAvailableProducts() {
        _isLoadingCatalogue.value = true
        Log.d("Books", "Sto fetchando i libri dal db")
        try {
            val response = repository.getCatalogue()

            if (response.isSuccessful && response.body() != null) {
                _allAvailableProducts.value = response.body()!!
            } else {
                throw Exception("Error fetching products")
            }

        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching all available products: Connection problem."
            else
                _errorMessage.value = "Error while fetching all available products."
        } finally {
            fetchHalloweenBooks()
            fetchRecentBooks()
            _isLoadingCatalogue.value = false
        }
    }

    suspend fun fetchBooksByFilter(filter: BookFilter): List<Book> {
        _isLoadingFilteredBooks.value = true
        try {
            val response = repository.getFilteredBooks(filter)

            if (response.isSuccessful && response.body() != null) {
                return response.body()!!
            } else {
                _errorMessage.value = "Error while fetching products."
            }

        }catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching products: Connection problem."
            else
                _errorMessage.value = "Error while fetching products."
        }finally {
            _isLoadingFilteredBooks.value = false
        }
        return emptyList()
    }

    fun setOrderOption(option: String){
        _sortOption.value = option
    }

    fun sortProducts(products: List<Book>): List<Book> {
        Log.d("SORT BOOKS", "sort by: ${_sortOption.value} sortProducts: $products ")
        return if (products.isNotEmpty()) {
            when (_sortOption.value) {
                "Price: Low to High" -> products.sortedBy { it.price }
                "Price: High to Low" -> products.sortedByDescending { it.price }
                "Weight: Low to High" -> products.sortedBy { it.weight }
                "Weight: High to Low" -> products.sortedByDescending { it.weight }
                "Number of pages: Low to High" -> products.sortedBy { it.pages }
                "Number of pages: High to Low" -> products.sortedByDescending { it.pages }
                "Age: Low to High" -> products.sortedBy { it.age }
                "Age: High to Low" -> products.sortedByDescending { it.age }
                "Newest" -> products.sortedByDescending { it.publishDate }
                "Oldest" -> products.sortedBy { it.publishDate }
                else -> products
            }
        } else {
            products
        }
    }

    suspend fun loadBook(id: Long) {
        _isLoadingBook.value = true
        try {
            bookCache.get(id)?.let {
                _bookFlow.value = it
                return
            }

            val response = repository.getBook(id)

            if (response.isSuccessful && response.body() != null) {
                _bookFlow.value = response.body()
                _bookFlow.value?.let { bookCache.put(it.id,_bookFlow.value) }
            } else {
                _bookFlow.value = null
                _errorMessage.value = "Error while loading book."
            }

        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while loading book: Connection problem."
            else
                _errorMessage.value = "Error while loading book."
        }finally {
            _isLoadingBook.value = false
        }
    }

    fun insertBook(book: SaveBook){

        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    repository.insertBook(book)

                    clearCache()
                    fetchAllAvailableProducts()
                }
                else
                    _errorMessage.value = "Session Error."
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error inserting book: Connection problem."
                else
                    _errorMessage.value = "Error inserting book."
            }
        }
    }

    fun updatePrice(newPrice: Double, bookId: Long){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {

                    val response = repository.updatePrice(bookId, Price(newPrice))

                    if(response.isSuccessful) {
                        clearCache()
                        loadBook(bookId)
                    }else{
                        val errorBody = response.errorBody()?.string()

                        _errorMessage.value = ErrorMessageParser(errorBody)
                    }
                }
                else
                    _errorMessage.value = "Session Error."
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error updating book price: Connection problem."
                else
                    _errorMessage.value = "Error updating book price."
            }
        }
    }

    fun restock(newStock: Int, bookId: Long){
        viewModelScope.launch {
            try {
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    val response = repository.updateStock(bookId, Stock(newStock))

                    if(response.isSuccessful) {
                        clearCache()
                        loadBook(bookId)
                    }else{
                        val errorBody = response.errorBody()?.string()

                        _errorMessage.value = ErrorMessageParser(errorBody)
                    }
                }
                else
                    _errorMessage.value = "Session Error."
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error updating book stock: Connection problem."
                else
                    _errorMessage.value = "Error updating book stock."
            }
        }
    }


    fun removeBook(bookId: Long){
        viewModelScope.launch {
            try{
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    repository.removeBook(bookId)

                    _allProducts.value = emptyList()

                    clearCache()
                    loadBook(bookId)
                }
                else{
                    _errorMessage.value = "Session Error."
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while deleting book: Connection problem."
                else
                    _errorMessage.value = "Error while deleting book."
            }
        }
    }

    fun restoreBook(bookId: Long){
        viewModelScope.launch {
            try{
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    repository.restoreBook(bookId)

                    _allProducts.value = emptyList()

                    clearCache()
                    loadBook(bookId)
                }
                else{
                    _errorMessage.value = "Session Error."
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while restoring book: Connection problem."
                else
                    _errorMessage.value = "Error while restoring book."
            }
        }
    }

    fun updateBookCover(bookId: Long, cover: File){
        viewModelScope.launch {
            try{
                if (SessionManager.user != null && SessionManager.user!!.role == "ROLE_ADMIN") {
                    repository.updateCover(bookId, cover)

                    clearCache()
                    loadBook(bookId)
                }
                else{
                    _errorMessage.value = "Session Error."
                }
            } catch (e: Exception) {
                if (e is SocketTimeoutException)
                    _errorMessage.value = "Error while updating book cover: Connection problem."
                else
                    _errorMessage.value = "Error while updating book cover."
            }
        }
    }

    private fun clearCache(){
        imageCache.clearMemory()
        bookCache.clearMemory()
    }

    suspend fun fetchImage(url: String?): Bitmap? {
        if(url == null)
            return null
        try {
            imageCache.get(url)?.let {
                return it
            }

            val response = repository.getCoverImage(url)

            Log.d("IMAGE DEBUG", "response: ${response.message()}")

            if (response.isSuccessful && response.body() != null) {
                Log.d("IMAGE DEBUG", "response is not null")
                val inputStream = response.body()!!.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)

                if(bitmap != null)
                    imageCache.put(url, bitmap)

                return bitmap
            }else{
                _errorMessage.value = "Error while fetching book cover."
            }
        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching book cover: Connection problem."
            else
                _errorMessage.value = "Error while fetching book cover."
        }
        return null
    }

    suspend fun fetchHalloweenBooks() {
        try {
            _halloweenBooks.value = fetchBooksByFilter(BookFilter(genre = BookGenre.HORROR))
        }catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching books: Connection problem."
            else
                _errorMessage.value = "Error while fetching books."
        }
    }

    suspend fun fetchRecentBooks() {
        try {
            _recentBooks.value = fetchBooksByFilter(
                BookFilter(
                    maxPublishDate = LocalDate.now(),
                    minPublishDate = LocalDate.now().minusYears(4)
                )
            )
        }catch (e: Exception) {
            if (e is SocketTimeoutException)
                _errorMessage.value = "Error while fetching books: Connection problem."
            else
                _errorMessage.value = "Error while fetching books."
        }
    }

    fun localFilter(books: List<Book>, filter: BookFilter): List<Book> {
        return books.filter { book ->
            val matchesTitle = filter.title?.let { book.title.contains(it, ignoreCase = true) } ?: true
            val matchesAuthor = filter.author?.let { book.author.contains(it, ignoreCase = true) } ?: true
            val matchesPublisher = filter.publisher?.let{book.publisher.contains(it, ignoreCase = true)} ?: true

            matchesTitle || matchesAuthor || matchesPublisher
        }
    }

    fun onSnackbarDismissed(){
        _errorMessage.value = null
    }

    fun onLogout(){
        //viewModelScope.cancel()
        _sortOption.value = "Newest"
        _bookFlow.value = null
    }
}