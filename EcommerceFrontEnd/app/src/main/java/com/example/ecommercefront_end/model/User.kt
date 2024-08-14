package com.example.ecommercefront_end.model

import java.time.LocalDate
import java.util.ArrayList
import java.util.UUID

data class User(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val credentials: Credentials,
    val birthdate: LocalDate,
    val phoneNumber : String,
    val addresses: ArrayList<Address>
)