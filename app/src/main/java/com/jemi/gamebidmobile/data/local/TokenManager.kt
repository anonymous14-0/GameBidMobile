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

    fun clearToken() {
        prefs.edit()
            .remove("token")
            .apply()
    }
}