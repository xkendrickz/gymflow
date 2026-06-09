package com.example.gymflow_android

import com.example.gymflow_android.adapters.PresensiGymAdapter
import com.example.gymflow_android.models.PresensiGymItem
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class PresensiGymAdapterTest {

    private val onCheckin: (PresensiGymItem) -> Unit = mockk(relaxed = true)

    // use constructor instead of setData
    private fun makeAdapter(items: List<PresensiGymItem>): PresensiGymAdapter {
        return PresensiGymAdapter(onCheckin, items)
    }

    @Test
    fun `getItemCount returns correct count`() {
        val adapter = makeAdapter(listOf(
            PresensiGymItem(1, "Budi", "2025-05-29", "07:00:00", 0),
            PresensiGymItem(2, "Sari", "2025-05-29", "09:00:00", 1),
        ))
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun `markAsHadir updates status correctly`() {
        val adapter = makeAdapter(listOf(
            PresensiGymItem(1, "Budi", "2025-05-29", "07:00:00", 0),
        ))
        adapter.markAsHadir(1)
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun `setData with empty list gives zero count`() {
        val adapter = makeAdapter(emptyList())
        assertEquals(0, adapter.itemCount)
    }
}