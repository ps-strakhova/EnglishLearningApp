package com.example.englishlearningapp.data

import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity

val sampleTopicsIcons = mapOf(
    "Фрукты" to "🍎",
    "Транспорт" to "🚗",
    "Приветствия" to "👋",
    "Животные" to "🐶",
    "Еда" to "🍞",
    "Работа" to "💼",
    "Эмоции" to "😊",
    "Дом" to "🏠",
    "Одежда" to "👕",
    "Природа" to "🌳"
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
    WordEntity(word = "Grapes", translation = "Виноград", topic = "Фрукты", difficulty = "medium", icon = "🍇", example = "Eat fresh grapes"),

    // Транспорт
    WordEntity(word = "Car", translation = "Машина", topic = "Транспорт", difficulty = "medium", isLearned = true, icon = "🚗", example = "Drive a car"),
    WordEntity(word = "Train", translation = "Поезд", topic = "Транспорт", difficulty = "medium", icon = "🚆", example = "Take the train"),
    WordEntity(word = "Airplane", translation = "Самолёт", topic = "Транспорт", difficulty = "hard", isFavorite = true, icon = "✈️", example = "Fly by airplane"),
    WordEntity(word = "Bicycle", translation = "Велосипед", topic = "Транспорт", difficulty = "easy", icon = "🚲", example = "Ride a bicycle"),
    WordEntity(word = "Boat", translation = "Лодка", topic = "Транспорт", difficulty = "medium", icon = "⛵", example = "Sail the boat"),

    // Приветствия
    WordEntity(word = "Hello", translation = "Привет", topic = "Приветствия", difficulty = "easy", isLearned = true, icon = "👋", example = "Hello, friend!"),
    WordEntity(word = "Goodbye", translation = "До свидания", topic = "Приветствия", difficulty = "easy", icon = "👋", example = "Say goodbye"),
    WordEntity(word = "Good morning", translation = "Доброе утро", topic = "Приветствия", difficulty = "medium", icon = "☀️", example = "Good morning, everyone"),
    WordEntity(word = "Good night", translation = "Спокойной ночи", topic = "Приветствия", difficulty = "medium", isLearned = true, icon = "🌙", example = "Good night, sleep well"),
    WordEntity(word = "Hi", translation = "Привет", topic = "Приветствия", difficulty = "easy", icon = "👋", example = "Say hi to your friend"),

    // Животные
    WordEntity(word = "Dog", translation = "Собака", topic = "Животные", difficulty = "easy", isLearned = true, icon = "🐶", example = "The dog barks"),
    WordEntity(word = "Cat", translation = "Кошка", topic = "Животные", difficulty = "easy", icon = "🐱", example = "The cat sleeps"),
    WordEntity(word = "Elephant", translation = "Слон", topic = "Животные", difficulty = "hard", icon = "🐘", example = "The elephant is big"),
    WordEntity(word = "Lion", translation = "Лев", topic = "Животные", difficulty = "medium", isFavorite = true, icon = "🦁", example = "The lion roars"),
    WordEntity(word = "Tiger", translation = "Тигр", topic = "Животные", difficulty = "hard", icon = "🐯", example = "The tiger hunts"),

    // Еда
    WordEntity(word = "Bread", translation = "Хлеб", topic = "Еда", difficulty = "easy", icon = "🍞", example = "Eat fresh bread"),
    WordEntity(word = "Cheese", translation = "Сыр", topic = "Еда", difficulty = "medium", isLearned = true, icon = "🧀", example = "Slice the cheese"),
    WordEntity(word = "Pizza", translation = "Пицца", topic = "Еда", difficulty = "medium", icon = "🍕", example = "Delicious pizza"),
    WordEntity(word = "Apple pie", translation = "Яблочный пирог", topic = "Еда", difficulty = "hard", icon = "🥧", example = "Bake apple pie"),
    WordEntity(word = "Salad", translation = "Салат", topic = "Еда", difficulty = "easy", icon = "🥗", example = "Prepare fresh salad"),
//
//    // Одежда
//    WordEntity(word = "Shirt", translation = "Рубашка", topic = "Одежда", difficulty = "easy", icon = "👕", example = "Wear a clean shirt"),
//    WordEntity(word = "Pants", translation = "Брюки", topic = "Одежда", difficulty = "easy", icon = "👖", example = "New pants"),
//    WordEntity(word = "Dress", translation = "Платье", topic = "Одежда", difficulty = "medium", icon = "👗", example = "Beautiful dress"),
//    WordEntity(word = "Jacket", translation = "Куртка", topic = "Одежда", difficulty = "medium", icon = "🧥", example = "Warm jacket"),
//    WordEntity(word = "Shoes", translation = "Обувь", topic = "Одежда", difficulty = "easy", icon = "👟", example = "Comfortable shoes"),
//
//    // Дом
//    WordEntity(word = "House", translation = "Дом", topic = "Дом", difficulty = "easy", isLearned = true, icon = "🏠", example = "My house is big"),
//    WordEntity(word = "Room", translation = "Комната", topic = "Дом", difficulty = "easy", icon = "🛋️", example = "Clean the room"),
//    WordEntity(word = "Kitchen", translation = "Кухня", topic = "Дом", difficulty = "medium", icon = "🍽️", example = "Cook in the kitchen"),
//    WordEntity(word = "Bathroom", translation = "Ванная", topic = "Дом", difficulty = "medium", icon = "🛁", example = "Go to the bathroom"),
//    WordEntity(word = "Bedroom", translation = "Спальня", topic = "Дом", difficulty = "easy", icon = "🛏️", example = "Sleep in the bedroom"),
//
//    // Природа
//    WordEntity(word = "Sun", translation = "Солнце", topic = "Природа", difficulty = "easy", icon = "☀️", example = "The sun shines"),
//    WordEntity(word = "Moon", translation = "Луна", topic = "Природа", difficulty = "easy", icon = "🌙", example = "Full moon tonight"),
//    WordEntity(word = "Tree", translation = "Дерево", topic = "Природа", difficulty = "easy", icon = "🌳", example = "Tall tree"),
//    WordEntity(word = "River", translation = "Река", topic = "Природа", difficulty = "medium", icon = "🏞️", example = "Swim in the river"),
//    WordEntity(word = "Mountain", translation = "Гора", topic = "Природа", difficulty = "hard", icon = "⛰️", example = "Climb the mountain"),
//
//    // Работа
//    WordEntity(word = "Job", translation = "Работа", topic = "Работа", difficulty = "easy", icon = "💼", example = "Find a job"),
//    WordEntity(word = "Office", translation = "Офис", topic = "Работа", difficulty = "easy", icon = "🏢", example = "Work in an office"),
//    WordEntity(word = "Boss", translation = "Начальник", topic = "Работа", difficulty = "medium", icon = "🧑‍💼", example = "Talk to the boss"),
//    WordEntity(word = "Meeting", translation = "Встреча", topic = "Работа", difficulty = "medium", icon = "📊", example = "Attend a meeting"),
//    WordEntity(word = "Salary", translation = "Зарплата", topic = "Работа", difficulty = "hard", icon = "💰", example = "Get a salary"),
//
//    // Эмоции
//    WordEntity(word = "Happy", translation = "Счастливый", topic = "Эмоции", difficulty = "easy", icon = "😊", example = "Feel happy"),
//    WordEntity(word = "Sad", translation = "Грустный", topic = "Эмоции", difficulty = "easy", icon = "😢", example = "Feel sad"),
//    WordEntity(word = "Angry", translation = "Злой", topic = "Эмоции", difficulty = "medium", icon = "😠", example = "Be angry"),
//    WordEntity(word = "Excited", translation = "Взволнованный", topic = "Эмоции", difficulty = "medium", icon = "🤩", example = "Feel excited"),
//    WordEntity(word = "Calm", translation = "Спокойный", topic = "Эмоции", difficulty = "easy", icon = "😌", example = "Stay calm")

)


