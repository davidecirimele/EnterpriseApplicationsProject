package com.example.ecommercefront_end.model

data class Page<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
    val number: Int
)
