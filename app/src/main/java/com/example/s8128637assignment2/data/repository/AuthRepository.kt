package com.example.s8128637assignment2.data.repository

import com.example.s8128637assignment2.data.ApiResult

interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResult<String>
}
