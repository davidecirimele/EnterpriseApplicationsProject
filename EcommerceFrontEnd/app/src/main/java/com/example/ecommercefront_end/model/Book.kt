package com.example.ecommercefront_end.model

import java.time.LocalDate

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val ISBN: String,
    val pages: Int,
    val edition: String,
    val format: String,
    val genre: String,
    val language: String,
    val publisher: String,
    val age: Int,
    val publishDate: LocalDate,
    val category: String,
    val weight: Double,
    val price: Double,
    val stock: Int,
)
