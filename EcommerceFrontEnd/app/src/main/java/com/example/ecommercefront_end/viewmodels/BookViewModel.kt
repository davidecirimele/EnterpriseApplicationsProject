package com.example.ecommercefront_end.viewmodels

import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.Address
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

class BookViewModel(private val repository: BookRepository): ViewModel() {

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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    fun fetchBooksData(){
        viewModelScope.launch {
            try {

                // Usa async per le chiamate parallele
                val priceJob = async { fetchPrice() }
                val ageJob = async { fetchAge() }
                val pagesJob = async { fetchPages() }

                // Aspetta che tutte le chiamate siano completate
                priceJob.await()
                ageJob.await()
                pagesJob.await()

            } catch (e: Exception) {
                Log.e("Book Error", "Error loading books data", e)
            }finally {
                _isLoading.value = false // Ferma loading dopo che tutte le operazioni sono completate
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


}