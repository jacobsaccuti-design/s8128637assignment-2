package com.example.s8128637assignment2.data.remote.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val keypass: String
)
