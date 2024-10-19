package com.example.ecommercefront_end.model

data class Wishlist(

    val id: Long,
    val items: List<WishlistItem>,
    var name: String,
    val user: User?,
    var group: Group,
    var privacySetting: WishlistPrivacy,
    val wishlistToken: String
)


