package com.example.englishlearningapp.data

import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity

val sampleTopicsIcons = mapOf(
    "Фрукты" to "🍎",
    "Транспорт" to "🚗",
    "Приветствия" to "👋"
)

suspend fun seedDatabaseIfEmpty(dao: WordDao) {
    val count = dao.getTotalWordsCount()
    if (count == 0) {
        dao.insertWords(sampleWords)
    }
}

suspend fun getSeedTopicsWithCounts(dao: WordDao): List<TopicItem> {
    val topics = dao.getTopics()
    return topics.map { topic ->
        val total = dao.getWordsCountByTopic(topic)
        val learned = dao.getLearnedWordsCountByTopic(topic)
        TopicItem(
            title = topic,
            iconTopic = sampleTopicsIcons[topic] ?: "📚",
            totalWords = total,
            learnedWords = learned
        )
    }
}

val sampleWords = listOf(
    WordEntity(word = "Apple", translation = "Яблоко", topic = "Фрукты", difficulty = "easy", isLearned = true, icon = "🍎"),
    WordEntity(word = "Banana", translation = "Банан", topic = "Фрукты", difficulty = "easy", icon = "🍌"),
    WordEntity(word = "Car", translation = "Машина", topic = "Транспорт", difficulty = "medium", isLearned = true, icon = "🚗"),
    WordEntity(word = "Train", translation = "Поезд", topic = "Транспорт", difficulty = "medium", icon = "🚆"),
    WordEntity(word = "Hello", translation = "Привет", topic = "Приветствия", difficulty = "easy", isLearned = true, icon = "👋"),
    WordEntity(word = "Goodbye", translation = "До свидания", topic = "Приветствия", difficulty = "easy", icon = "👋")
)


