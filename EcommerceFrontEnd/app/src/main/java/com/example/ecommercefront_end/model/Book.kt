package com.example.ecommercefront_end.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Multipart
import java.io.File
import java.time.LocalDate

data class Book(
    val id: Long,
    val title: String,
    val author: String,

    @SerializedName("isbn")
    val ISBN: String, // Annotazione per mappare la proprietà "isbn"

    val pages: Int,
    val edition: String,
    val format: BookFormat,
    val genre: BookGenre,
    val language: BookLanguage,
    val publisher: String,
    val age: Int,
    val publishDate: LocalDate, // Annotazione per mappare la proprietà "publish_date"
    val weight: Double,
    val price: Double,
    val stock: Int,
    //val image: ByteArray,
    val available: Boolean
)
