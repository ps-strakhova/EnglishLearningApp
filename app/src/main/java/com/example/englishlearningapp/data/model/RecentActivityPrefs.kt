package com.example.englishlearningapp.data.model

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object RecentActivityPrefs {

    private const val PREFS = "recent_activity"
    private const val KEY_LIST = "items"
    private const val MAX_ITEMS = 10

    fun add(context: Context, item: ActivityItem) {
        val list = getAll(context).toMutableList()
        list.add(0, item)

        if (list.size > MAX_ITEMS) list.removeLast()

        val arr = JSONArray()
        list.forEach {
            val o = JSONObject()
            o.put("icon", it.iconEmoji)
            o.put("title", it.title)
            o.put("category", it.category)
            o.put("timestamp", it.timestamp)
            o.put("description", it.description) // ✅
            o.put("points", it.points)
            o.put("correct", it.correct)
            o.put("total", it.total)
            arr.put(o)
        }

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LIST, arr.toString())
            .apply()
    }

    fun getAll(context: Context): List<ActivityItem> {
        val raw = context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_LIST, null) ?: return emptyList()

        val arr = JSONArray(raw)
        val result = mutableListOf<ActivityItem>()

        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)

            val icon = o.optString("icon", "📚")
            val title = o.optString("title", "")
            val category = o.optString("category", "")
            val timestamp = o.optLong("timestamp", System.currentTimeMillis())
            val description = o.optString("description", "")
            val points = o.optInt("points", 0)

            // 🔥 ВОТ ГЛАВНОЕ ИСПРАВЛЕНИЕ
            val correct = o.optInt("correct", 0)
            val total = o.optInt("total", 0)

            result.add(
                ActivityItem(
                    iconEmoji = icon,
                    title = title,
                    category = category,
                    timestamp = timestamp,
                    description = description,
                    points = points,
                    correct = correct,
                    total = total
                )
            )
        }

        return result
    }
}
