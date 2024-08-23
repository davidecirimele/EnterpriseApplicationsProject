package com.example.ecommercefront_end.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

data class Book(
    val id: Long,
    val title: String,
    val author: String,

    @SerializedName("isbn")
    val ISBN: String, // Annotazione per mappare la proprietà "isbn"

    val pages: Int,
    val edition: String,
    val format: BookFormat? = null, // Proprietà opzionale
    val genre: BookGenre? = null, // Proprietà opzionale
    val language: BookLanguage? = null, // Proprietà opzionale
    val publisher: String,
    val age: Int,
    val publishDate: LocalDate, // Annotazione per mappare la proprietà "publish_date"
    @SerializedName("insertDate")
    val insertDate: LocalDate, // Annotazione per mappare la proprietà "insert_date"
    val weight: Double,
    val price: Double,
    val stock: Int,
)
