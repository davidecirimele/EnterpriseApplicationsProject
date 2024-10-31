package com.example.ecommercefront_end.model

import java.time.LocalDate
import java.util.ArrayList
import java.util.UUID

data class User(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthdate: LocalDate,
    val phoneNumber : String,
    val role: String,
)