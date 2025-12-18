package com.example.englishlearningapp.data

object TopicIcons {

    private val icons = mapOf(
        "Фрукты" to "🍎",
        "Транспорт" to "🚗",
        "Приветствия" to "👋"
    )

    fun getIcon(topic: String?): String {
        return if (topic == null) {
            "⭐" // общий тест
        } else {
            icons[topic] ?: "📘"
        }
    }
}


