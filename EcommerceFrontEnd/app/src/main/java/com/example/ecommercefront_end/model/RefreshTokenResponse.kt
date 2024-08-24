package com.example.ecommercefront_end.model

import org.jetbrains.annotations.NotNull

data class RefreshTokenResponse(

    val accessToken: String,
    val refreshToken: String
)
