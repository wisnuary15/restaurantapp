package com.example.restaurantapp.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val PREF_NAME = "user"

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        val editor = customPreference(context, PREF_NAME).edit()
        editor.putBoolean("loggedIn", loggedIn)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return customPreference(context, PREF_NAME).getBoolean("loggedIn", false)
    }

    fun setUsername(context: Context, username: String) {
        val editor = customPreference(context, PREF_NAME).edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        return customPreference(context, PREF_NAME).getString("username", null)
    }

    fun clearUserDetails(context: Context) {
        val editor = customPreference(context, PREF_NAME).edit()
        editor.clear()
        editor.apply()
    }
}
