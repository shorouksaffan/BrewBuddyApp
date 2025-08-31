package com.example.brewbuddy.core.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefs @Inject constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private companion object {
        const val KEY_USER_NAME = "user_name"
        const val KEY_FIRST_LAUNCH = "first_launch"
        const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    }

    var userName: String
        get() = sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
        set(value) = sharedPreferences.edit { putString(KEY_USER_NAME, value) }

    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = sharedPreferences.edit { putBoolean(KEY_FIRST_LAUNCH, value) }

    var isOnboardingComplete: Boolean
        get() = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETE, false)
        set(value) = sharedPreferences.edit { putBoolean(KEY_ONBOARDING_COMPLETE, value) }

    // Remove the generic saveData/getData methods to avoid type safety issues
    // Use specific properties instead for better type safety and readability

    fun completeOnboarding(userName: String) {
        sharedPreferences.edit {
            putString(KEY_USER_NAME, userName)
            putBoolean(KEY_ONBOARDING_COMPLETE, true)
            putBoolean(KEY_FIRST_LAUNCH, false)
        }
    }

    fun clearUserData() {
        sharedPreferences.edit {
            remove(KEY_USER_NAME)
            putBoolean(KEY_FIRST_LAUNCH, false)
            putBoolean(KEY_ONBOARDING_COMPLETE, false)
        }
    }

    fun hasUserName(): Boolean {
        return userName.isNotBlank()
    }
}