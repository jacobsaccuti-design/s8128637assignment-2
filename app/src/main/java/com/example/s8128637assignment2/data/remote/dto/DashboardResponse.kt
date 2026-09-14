package com.example.s8128637assignment2.data.remote.dto

/**
 * The entity schema differs per topic/keypass, so each entity is kept as a
 * generic key-value map (Gson deserializes a JSON object into Map<String, Any>
 * out of the box) rather than a fixed data class.
 */
data class DashboardResponse(
    val entities: List<Map<String, Any>>,
    val entityTotal: Int
)
