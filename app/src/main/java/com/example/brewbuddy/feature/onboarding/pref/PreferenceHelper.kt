package com.example.brewbuddy.feature.onboarding.pref

import android.content.Context

object PreferenceHelper {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_NAME = "user_name"

    fun saveUserName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserName(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, null)
    }
}