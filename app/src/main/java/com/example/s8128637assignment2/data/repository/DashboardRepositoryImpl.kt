package com.example.s8128637assignment2.data.repository

import com.example.s8128637assignment2.data.ApiResult
import com.example.s8128637assignment2.data.remote.Nit3213ApiService
import com.example.s8128637assignment2.data.remote.dto.DashboardResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: Nit3213ApiService
) : DashboardRepository {

    override suspend fun getDashboard(keypass: String): ApiResult<DashboardResponse> {
        return try {
            ApiResult.Success(api.getDashboard(keypass))
        } catch (e: HttpException) {
            val message = when (e.code()) {
                404 -> "No dashboard data found for this session"
                in 500..599 -> "Server error, please try again later"
                else -> "Failed to load dashboard (code ${e.code()})"
            }
            ApiResult.Error(message)
        } catch (e: IOException) {
            ApiResult.Error("Unable to reach the server. Check your connection.")
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
