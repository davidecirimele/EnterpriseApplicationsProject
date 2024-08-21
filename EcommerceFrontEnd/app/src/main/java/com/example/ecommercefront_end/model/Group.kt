package com.example.ecommercefront_end.model


data class Group(
    val id: Long,
    val groupName: String,
    val members: List<User>
)