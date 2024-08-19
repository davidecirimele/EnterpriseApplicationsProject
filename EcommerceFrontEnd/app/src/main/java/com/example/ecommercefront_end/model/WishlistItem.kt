package com.example.ecommercefront_end.model;

import android.provider.ContactsContract;

data class WishlistItem (
    val id: Long,
    val product: Product,
    val wishlist: Wishlist,
    val quantity: Int


)