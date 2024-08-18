package com.example.ecommercefront_end

import com.example.ecommercefront_end.model.User

object SessionManager {

    var user: User? = null
    private set

    var authToken: String? = null
    private set

}