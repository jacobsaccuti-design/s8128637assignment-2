package com.example.s8128637assignment2.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EntityUiMapperTest {

    private val entity = linkedMapOf<String, Any?>(
        "property1" to "value1",
        "property2" to "value2",
        "description" to "Detailed description"
    )

    @Test
    fun `title returns the first non-description field`() {
        assertEquals("value1", EntityUiMapper.title(entity))
    }

    @Test
    fun `subtitle excludes description and the title field`() {
        val subtitle = EntityUiMapper.subtitle(entity)
        assertTrue(subtitle.contains("property2: value2"))
        assertFalse(subtitle.contains("description"))
        assertFalse(subtitle.contains("property1"))
    }

    @Test
    fun `description returns the description field`() {
        assertEquals("Detailed description", EntityUiMapper.description(entity))
    }

    @Test
    fun `description falls back when missing`() {
        val noDescription = linkedMapOf<String, Any?>("property1" to "value1")
        assertEquals("No description available", EntityUiMapper.description(noDescription))
    }

    @Test
    fun `allFieldsOrdered excludes description`() {
        val fields = EntityUiMapper.allFieldsOrdered(entity)
        assertEquals(listOf("property1" to "value1", "property2" to "value2"), fields)
    }
}
