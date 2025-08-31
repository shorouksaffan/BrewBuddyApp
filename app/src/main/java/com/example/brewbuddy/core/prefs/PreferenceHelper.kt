package com.example.brewbuddy.core.prefs

import android.content.Context

object PreferenceHelper {

    private const val PREFS_NAME = "brewbuddy_prefs"
    private const val KEY_USER_NAME = "key_user_name"

    fun saveUserName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserName(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun deleteUserName(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_USER_NAME).apply()
    }
}
