package com.example.ecommercefront_end.model

import java.time.LocalDate
import java.util.UUID

data class User(
    val id : UUID,
    val lastname : String,
    val firstname : String,
    val email : String,
    val birthDate : LocalDate,
    val phoneNumber : String,

    //val addresses : List<Address>,



    )
