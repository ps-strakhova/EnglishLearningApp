package com.example.englishlearningapp.data.model

data class TopicConfig(
    val title: String,
    val icon: String
)

val allTopics = listOf(
    TopicConfig("Фрукты", "🍎"),
    TopicConfig("Транспорт", "🚗"),
    TopicConfig("Приветствия", "👋"),
    TopicConfig("Животные", "🐶"),
    TopicConfig("Еда", "🍞"),
    TopicConfig("Одежда", "👕"),
    TopicConfig("Дом", "🏠"),
    TopicConfig("Природа", "🌳"),
    TopicConfig("Работа", "💼"),
    TopicConfig("Эмоции", "😊")
)
