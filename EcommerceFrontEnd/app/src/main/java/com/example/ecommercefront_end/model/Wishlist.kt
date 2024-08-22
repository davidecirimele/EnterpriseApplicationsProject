package com.example.ecommercefront_end.model

data class Wishlist (

    val id: Long,
    val items: List<WishlistItem>,
    val name: String,
    val user: User,
    val group: Group,
    val privacySetting: String,
)

