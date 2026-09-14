package com.example.s8128637assignment2.data.repository

import com.example.s8128637assignment2.data.ApiResult
import com.example.s8128637assignment2.data.remote.dto.DashboardResponse

interface DashboardRepository {
    suspend fun getDashboard(keypass: String): ApiResult<DashboardResponse>
}
