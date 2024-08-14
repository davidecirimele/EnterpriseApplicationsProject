package com.example.ecommercefront_end.model

data class Wishlist (

    val id: Long,
    val items: List<Product> = listOf(),
    val name: String,
    val user: User,
    val group: List<User> = listOf(),
    val privacySetting: String,
)