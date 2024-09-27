package com.example.ecommercefront_end.model

data class Address(

    val id: Long,

    val street: String,

    val province: String,

    val city: String,

    val state: String,

    val postalCode: String,

    val additionalInfo: String,

    val defaultAddress: Boolean
)
