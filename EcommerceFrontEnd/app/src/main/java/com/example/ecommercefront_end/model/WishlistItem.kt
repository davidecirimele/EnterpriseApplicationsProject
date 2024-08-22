package com.example.ecommercefront_end.model;

import android.provider.ContactsContract;

data class WishlistItem (

    val id: Long,
    val wishlistId: Long,
    val book: Book,
    val quantity: Int


)