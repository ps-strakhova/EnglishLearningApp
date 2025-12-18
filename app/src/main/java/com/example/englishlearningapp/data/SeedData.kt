package com.example.englishlearningapp.data

import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity

val sampleTopicsIcons = mapOf(
    "Фрукты" to "🍎",
    "Транспорт" to "🚗",
    "Приветствия" to "👋",
    "Животные" to "🐶",
    "Еда" to "🍞"
)

suspend fun seedDatabaseIfEmpty(dao: WordDao) {
    val count = dao.getTotalWordsCount()
    if (count == 0) {
        dao.insertWordsIgnore(sampleWords)
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
    // Фрукты
    WordEntity(word = "Apple", translation = "Яблоко", topic = "Фрукты", difficulty = "easy", isLearned = true, icon = "🍎", example = "I eat an apple"),
    WordEntity(word = "Banana", translation = "Банан", topic = "Фрукты", difficulty = "easy", icon = "🍌", example = "Peel the banana"),
    WordEntity(word = "Cherry", translation = "Вишня", topic = "Фрукты", difficulty = "medium", icon = "🍒", example = "Sweet cherry pie"),
    WordEntity(word = "Pineapple", translation = "Ананас", topic = "Фрукты", difficulty = "hard", isFavorite = true, icon = "🍍", example = "Cut the pineapple"),

    // Транспорт
    WordEntity(word = "Car", translation = "Машина", topic = "Транспорт", difficulty = "medium", isLearned = true, icon = "🚗", example = "Drive a car"),
    WordEntity(word = "Train", translation = "Поезд", topic = "Транспорт", difficulty = "medium", icon = "🚆", example = "Take the train"),
    WordEntity(word = "Airplane", translation = "Самолёт", topic = "Транспорт", difficulty = "hard", isFavorite = true, icon = "✈️", example = "Fly by airplane"),
    WordEntity(word = "Bicycle", translation = "Велосипед", topic = "Транспорт", difficulty = "easy", icon = "🚲", example = "Ride a bicycle"),

    // Приветствия
    WordEntity(word = "Hello", translation = "Привет", topic = "Приветствия", difficulty = "easy", isLearned = true, icon = "👋", example = "Hello, friend!"),
    WordEntity(word = "Goodbye", translation = "До свидания", topic = "Приветствия", difficulty = "easy", icon = "👋", example = "Say goodbye"),
    WordEntity(word = "Good morning", translation = "Доброе утро", topic = "Приветствия", difficulty = "medium", icon = "☀️", example = "Good morning, everyone"),
    WordEntity(word = "Good night", translation = "Спокойной ночи", topic = "Приветствия", difficulty = "medium", isLearned = true, icon = "🌙", example = "Good night, sleep well"),

    // Животные
    WordEntity(word = "Dog", translation = "Собака", topic = "Животные", difficulty = "easy", isLearned = true, icon = "🐶", example = "The dog barks"),
    WordEntity(word = "Cat", translation = "Кошка", topic = "Животные", difficulty = "easy", icon = "🐱", example = "The cat sleeps"),
    WordEntity(word = "Elephant", translation = "Слон", topic = "Животные", difficulty = "hard", icon = "🐘", example = "The elephant is big"),
    WordEntity(word = "Lion", translation = "Лев", topic = "Животные", difficulty = "medium", isFavorite = true, icon = "🦁", example = "The lion roars"),

    // Еда
    WordEntity(word = "Bread", translation = "Хлеб", topic = "Еда", difficulty = "easy", icon = "🍞", example = "Eat fresh bread"),
    WordEntity(word = "Cheese", translation = "Сыр", topic = "Еда", difficulty = "medium", isLearned = true, icon = "🧀", example = "Slice the cheese"),
    WordEntity(word = "Pizza", translation = "Пицца", topic = "Еда", difficulty = "medium", icon = "🍕", example = "Delicious pizza")
)


