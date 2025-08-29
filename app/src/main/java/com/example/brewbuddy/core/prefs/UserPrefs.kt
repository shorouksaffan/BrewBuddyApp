package com.example.brewbuddy.core.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefs @Inject constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getData(key: String): String {
        return sharedPreferences.getString(key, null) ?: ""
    }
}