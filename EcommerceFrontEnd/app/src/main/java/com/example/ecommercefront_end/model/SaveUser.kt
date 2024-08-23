package com.example.ecommercefront_end.model

import java.time.LocalDate
import java.util.UUID

data class SaveUser(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val credential: Credential,
    val phoneNumber : String,
)
