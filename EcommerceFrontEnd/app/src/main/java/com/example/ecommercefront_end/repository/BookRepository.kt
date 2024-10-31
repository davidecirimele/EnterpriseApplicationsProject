package com.example.ecommercefront_end.repository

import android.util.Log
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.Price
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.model.Stock
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.network.BooksApiService
import com.example.ecommercefront_end.network.RetrofitClient.booksApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

import java.util.UUID

class BookRepository(private val apiService : BooksApiService)  {

    suspend fun insertBook(book: SaveBook){
        Log.d("ADDING_BOOK_DEBUG", "Inserting book: $book")

        val titlePart = book.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val authorPart = book.author.toRequestBody("text/plain".toMediaTypeOrNull())
        val isbnPart = book.ISBN.toRequestBody("text/plain".toMediaTypeOrNull())
        val pagesPart = book.pages.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val editionPart = book.edition.toRequestBody("text/plain".toMediaTypeOrNull())
        val formatPart = book.format.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val genrePart = book.genre.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val languagePart = book.language.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val publisherPart = book.publisher.toRequestBody("text/plain".toMediaTypeOrNull())
        val agePart = book.age.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val publishDatePart = book.publishDate.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val weightPart = book.weight.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = book.price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = book.stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val coverPart = prepareFilePart(book.image)

        try {
            booksApiService.insertBook(
                titlePart, authorPart, isbnPart, pagesPart, editionPart,
                formatPart, genrePart, languagePart, publisherPart,
                agePart, publishDatePart, weightPart, pricePart, stockPart, coverPart
            )
        }catch (e: Exception){
            Log.d("ADDING_BOOK_DEBUG", "Exception: ${e.message}");
        }
    }

    suspend fun updateCover(bookId: Long, file: File){
        val filePart = prepareFilePart(file)

        try {
            booksApiService.updateCover(bookId, filePart)
        }catch (e: Exception){
            Log.d("UPDATING_BOOK_DEBUG", "Exception: ${e.message}");
        }
    }

    suspend fun updatePrice(bookId: Long, newPrice: Price) = apiService.updatePrice(bookId, newPrice)

    suspend fun updateStock(bookId: Long, newStock : Stock) = apiService.updateStock(bookId, newStock)

    suspend fun getMaxPrice() = apiService.getMaxPrice()

    suspend fun getMinPrice() = apiService.getMinPrice()

    suspend fun getMaxAge() = apiService.getMaxAge()

    suspend fun getMinAge() = apiService.getMinAge()

    suspend fun getMaxPages() = apiService.getMaxPages()

    suspend fun getMinPages() = apiService.getMinPages()

    suspend fun getMaxWeight() = apiService.getMaxWeight()

    suspend fun getMinWeight() = apiService.getMinWeight()

    suspend fun getMinPublicationYear() = apiService.getMinPublicationYear()

    suspend fun getFilteredBooks(filter: BookFilter) = apiService.getFilteredBooks(filter)

    suspend fun getAllBooks() = apiService.getAllBooks()

    suspend fun getBook(bookId: Long) = apiService.getBook(bookId)

    suspend fun getCatalogue() = apiService.getCatalogue()

    suspend fun getCoverImage(url: String) = apiService.getCoverImage(url)

    suspend fun removeBook(bookId: Long) = apiService.deleteBook(bookId)

    suspend fun restoreBook(bookId: Long) = apiService.restoreBook(bookId)

    private fun prepareFilePart(file: File? = null): MultipartBody.Part {
        if(file != null) {

            val mimeType = if (file.extension.lowercase() == "jpg" || file.extension.lowercase() == "jpeg") {
                "image/jpeg"
            } else if (file.extension.lowercase() == "png") {
                "image/png"
            } else {
                throw Exception("Formato immagine non supportato. Solo JPEG e PNG sono permessi.")
            }

            Log.d("ADDING_BOOK_DEBUG", "prepareFilePart: $file, MIME type: $mimeType, File size: ${file.length() / (1024 * 1024)} MB")
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("image", file.name, requestFile)
        }else{
            throw Exception("Cover Image missing")
        }
    }
}