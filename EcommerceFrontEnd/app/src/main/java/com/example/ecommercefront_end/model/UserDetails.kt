package com.example.ecommercefront_end.model

import java.time.LocalDate
import java.util.ArrayList
import java.util.UUID

data class UserDetails(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber : String
)