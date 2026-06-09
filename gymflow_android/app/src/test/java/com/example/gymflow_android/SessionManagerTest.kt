package com.example.gymflow_android

import android.content.Context
import android.content.SharedPreferences
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setup() {
        context         = mockk(relaxed = true)
        sharedPreferences = mockk(relaxed = true)
        editor          = mockk(relaxed = true)

        every { context.applicationContext } returns context
        every {
            context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
        } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.clear() }  returns editor
        every { editor.apply() }  just Runs

        SessionManager.init(context)
    }

    @Test
    fun `clearSession clears shared preferences`() {
        SessionManager.clearSession()

        verify { editor.clear() }
        verify { editor.apply() }
    }
}