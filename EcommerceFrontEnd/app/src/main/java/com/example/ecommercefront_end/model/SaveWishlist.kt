package com.example.ecommercefront_end.model

data class SaveWishlist(

    val name: String,
    val items: List<WishlistItem>?,
    val user: User?,
    val group: Group?,
    var privacySetting: WishlistPrivacy,
    val wishlistToken: String

)