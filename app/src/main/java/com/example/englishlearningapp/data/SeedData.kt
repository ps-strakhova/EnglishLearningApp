package com.example.englishlearningapp.data

import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity

val sampleTopicsIcons = mapOf(
    "Фрукты" to "🍎",
    "Транспорт" to "🚗",
    "Приветствия" to "👋"
)

// Функция для получения TopicItem с иконками и количеством слов
suspend fun getSeedTopicsWithCounts(dao: WordDao): List<TopicItem> {
    val topics = dao.getTopics() // Получаем уникальные темы из базы
    return topics.map { topic ->
        TopicItem(
            title = topic,
            iconTopic = sampleTopicsIcons[topic] ?: "📚", // дефолтная иконка
            totalWords = dao.getWordsCountByTopic(topic),
            learnedWords = dao.getLearnedWordsCountByTopic(topic)
        )
    }
}

val sampleWords = listOf(
    WordEntity(word = "Apple", translation = "Яблоко", topic = "Фрукты", difficulty = "easy"),
    WordEntity(word = "Banana", translation = "Банан", topic = "Фрукты", difficulty = "easy"),
    WordEntity(word = "Car", translation = "Машина", topic = "Транспорт", difficulty = "medium"),
    WordEntity(word = "Train", translation = "Поезд", topic = "Транспорт", difficulty = "medium"),
    WordEntity(word = "Hello", translation = "Привет", topic = "Приветствия", difficulty = "easy"),
    WordEntity(word = "Goodbye", translation = "До свидания", topic = "Приветствия", difficulty = "easy")
)

