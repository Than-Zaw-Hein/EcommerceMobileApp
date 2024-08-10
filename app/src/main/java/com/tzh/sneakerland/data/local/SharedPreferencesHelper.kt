package com.tzh.sneakerland.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class SharedPreferencesHelper(context: Context) {

    companion object {
        private const val PREFERENCES_FILE_KEY = "com.tzh.sneakerland.PREFERENCE_FILE_KEY"
        private const val FIRST_TIME = "First_time"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

    fun setFirstTime() {
        sharedPreferences.edit {
            putBoolean(FIRST_TIME, false)

        }
    }

    val isFirstTime = sharedPreferences.getBoolean(FIRST_TIME, true)


    // Optionally, you can add a method to clear all preferences
    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}