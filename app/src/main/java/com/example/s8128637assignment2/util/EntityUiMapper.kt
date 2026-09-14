package com.example.s8128637assignment2.util

/**
 * Entities returned by the dashboard endpoint have a schema that varies by topic,
 * so summaries/details are built generically from the map rather than fixed field names.
 */
object EntityUiMapper {
    const val DESCRIPTION_KEY = "description"

    fun title(entity: Map<String, Any?>): String =
        entity.entries.firstOrNull { it.key != DESCRIPTION_KEY }?.value?.toString() ?: "Untitled"

    fun subtitle(entity: Map<String, Any?>): String {
        val firstKey = entity.entries.firstOrNull { it.key != DESCRIPTION_KEY }?.key
        return entity.entries
            .filter { it.key != DESCRIPTION_KEY && it.key != firstKey }
            .joinToString(separator = "\n") { "${it.key}: ${it.value}" }
    }

    fun description(entity: Map<String, Any?>): String =
        entity[DESCRIPTION_KEY]?.toString() ?: "No description available"

    fun allFieldsOrdered(entity: Map<String, Any?>): List<Pair<String, String>> =
        entity.entries
            .filter { it.key != DESCRIPTION_KEY }
            .map { it.key to (it.value?.toString() ?: "") }
}
