package com.example.ecommercefront_end.model

import java.io.File

data class BookCartDto(
    val id: Long,
    val title: String,
    val author: String,
    val price: Double,
    val cover: ByteArray,

)
