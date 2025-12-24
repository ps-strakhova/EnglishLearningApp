package com.example.englishlearningapp.data.model

import android.content.Context

object UserProfilePrefs {

    private const val PREFS_NAME = "user_profile"
    private const val KEY_POINTS = "points"

    fun addPoints(context: Context, points: Int): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = prefs.getInt(KEY_POINTS, 0)

        prefs.edit()
            .putInt(KEY_POINTS, current + points)
            .apply()

        return points
    }

    fun getPoints(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_POINTS, 0)
    }
}
