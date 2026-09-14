package com.example.s8128637assignment2.data.remote

import com.example.s8128637assignment2.data.remote.dto.DashboardResponse
import com.example.s8128637assignment2.data.remote.dto.LoginRequest
import com.example.s8128637assignment2.data.remote.dto.LoginResponse
import com.example.s8128637assignment2.util.Constants
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Nit3213ApiService {

    @POST(Constants.AUTH_ENDPOINT)
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
