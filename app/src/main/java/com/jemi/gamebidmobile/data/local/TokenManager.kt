package com.jemi.gamebidmobile.data.local

import android.content.Context

class TokenManager(context: Context) {

    private val prefs =
        context.getSharedPreferences(
            "gamebid_prefs",
            Context.MODE_PRIVATE
        )

    fun saveToken(token: String) {
        prefs.edit()
            .putString("token", token)
            .apply()
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun saveRole(role: String) {
        prefs.edit()
            .putString("role", role)
            .apply()
    }

    fun getRole(): String? {
        return prefs.getString("role", null)
    }

    fun clearAll() {
        prefs.edit()
            .clear()
            .apply()
    }
}