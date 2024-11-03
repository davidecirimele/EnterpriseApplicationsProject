package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.Price
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.model.ShoppingCart
import com.example.ecommercefront_end.model.Stock
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.google.rpc.context.AttributeContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File
import java.time.LocalDate
import java.util.UUID

interface BooksApiService {

    @Multipart
    @POST("books/add")
    @RequiresAuth
    suspend fun insertBook(
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("ISBN") isbn: RequestBody,
        @Part("pages") pages: RequestBody,
        @Part("edition") edition: RequestBody,
        @Part("format") format: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("language") language: RequestBody,
        @Part("publisher") publisher: RequestBody,
        @Part("age") age: RequestBody,
        @Part("publishDate") publishDate: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part cover: MultipartBody.Part
    ): Response<Book>

    @GET("books/get/{idBook}")
    suspend fun getBook(@Path("idBook") idBook: Long) : Response<Book>

    @GET("books/getAll")
    @RequiresAuth
    suspend fun getAllBooks() : Response<List<Book>>

    @GET("books/get-catalogue")
    suspend fun getCatalogue() : Response<List<Book>>

    @GET("books/get/max-price")
    suspend fun getMaxPrice() : Response<Double>

    @GET("books/get/min-price")
    suspend fun getMinPrice() : Response<Double>

    @GET("books/get/max-age")
    suspend fun getMaxAge() : Response<Int>

    @GET("books/get/min-age")
    suspend fun getMinAge() : Response<Int>

    @GET("books/get/max-pages")
    suspend fun getMaxPages() : Response<Int>

    @GET("books/get/min-pages")
    suspend fun getMinPages() : Response<Int>

    @GET("books/get/max-weight")
    suspend fun getMaxWeight() : Response<Double>

    @GET("books/get/min-weight")
    suspend fun getMinWeight() : Response<Double>

    @GET("books/get/min-publication-date")
    suspend fun getMinPublicationYear() : Response<LocalDate>

    @PUT("books/delete/{idBook}")
    @RequiresAuth
    suspend fun deleteBook(@Path("idBook") idBook: Long)

    @PUT("books/restore/{idBook}")
    @RequiresAuth
    suspend fun restoreBook(@Path("idBook") idBook: Long)

    @POST("books/get/filter")
    suspend fun getFilteredBooks(@Body filter: BookFilter) : Response<List<Book>>

    @PUT("books/edit-price/{bookId}")
    @RequiresAuth
    suspend fun updatePrice(@Path("bookId") bookId: Long, @Body newPrice: Price): Response<Book>

    @PUT("books/edit-stock/{bookId}")
    @RequiresAuth
    suspend fun updateStock(@Path("bookId") bookId: Long, @Body newStock: Stock): Response<Book>

    @Multipart
    @RequiresAuth
    @PUT("books/{bookId}/update-cover")
    suspend fun updateCover(@Path("bookId") bookId: Long, @Part cover: MultipartBody.Part): Response<Book>

    @GET("books/get-cover/{filename}")
    suspend fun getCoverImage(@Path("filename") imageUrl: String): Response<ResponseBody>
}