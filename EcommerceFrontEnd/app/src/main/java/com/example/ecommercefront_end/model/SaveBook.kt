package com.example.ecommercefront_end.model

import com.google.gson.annotations.SerializedName
import java.io.File
import java.time.LocalDate

data class SaveBook(
    val title: String,
    val author: String,

    @SerializedName("isbn")
    val ISBN: String,

    val pages: Int,
    val edition: String,
    val format: BookFormat,
    val genre: BookGenre,
    val language: BookLanguage,
    val publisher: String,
    val age: Int,
    val publishDate: LocalDate,
    val weight: Double,
    val price: Double,
    val stock: Int,
    val image: File?
)
