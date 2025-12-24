package com.example.englishlearningapp.data.model

data class ActivityItem(
    val iconEmoji: String,
    val title: String,
    val category: String,
    val timestamp: Long,

    val description: String, // ✅ ВОЗВРАЩАЕМ

    val points: Int,
    val correct: Int,
    val total: Int
)



