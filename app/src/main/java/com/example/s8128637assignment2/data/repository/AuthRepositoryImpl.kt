package com.example.s8128637assignment2.data.repository

import com.example.s8128637assignment2.data.ApiResult
import com.example.s8128637assignment2.data.remote.Nit3213ApiService
import com.example.s8128637assignment2.data.remote.dto.LoginRequest
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: Nit3213ApiService
) : AuthRepository {

    override suspend fun login(username: String, password: String): ApiResult<String> {
        return try {
            val response = api.login(LoginRequest(username, password))
            ApiResult.Success(response.keypass)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                // The auth endpoint returns 404 (not 401/403) when credentials don't match.
                400, 401, 403, 404 -> "Invalid username or password"
                in 500..599 -> "Server error, please try again later"
                else -> "Login failed (code ${e.code()})"
            }
            ApiResult.Error(message)
        } catch (e: IOException) {
            ApiResult.Error("Unable to reach the server. Check your connection.")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
