package com.example.englishlearningapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.englishlearningapp.data.model.ActivityItem
import com.example.englishlearningapp.data.model.Word
import com.example.englishlearningapp.data.repository.WordRepository

class HomeViewModel : ViewModel() {

    private val repository = WordRepository()

    private val words: List<Word> = repository.getWords()

    // ===== Прогресс =====
    val learnedCount: Int
        get() = words.count { it.isLearned }

    val totalCount: Int
        get() = words.size

    val progressPercent: Int
        get() = if (totalCount == 0) 0 else (learnedCount * 100 / totalCount)

    // ===== Статистика =====
    val newWordsToday: Int = 20
    val streakDays: Int = 5

    // ===== Недавняя активность =====
    val recentActivities = listOf(
        ActivityItem(
            iconEmoji = "📚",
            title = "Вы выучили 5 новых слов",
            category = "Еда",
            timeAgo = "2 часа назад",
            description = "Вы успешно выучили новые слова по теме «Еда».",
            points = 100
        ),
        ActivityItem(
            iconEmoji = "✅",
            title = "Дневная цель выполнена",
            category = "Приветствия",
            timeAgo = "5 часов назад",
            description = "Поздравляем! Вы достигли своей дневной цели обучения.",
            points = 100
        ),
        ActivityItem(
            iconEmoji = "⭐",
            title = "Идеальный результат",
            category = "Путешествия",
            timeAgo = "1 день назад",
            description = "Вы ответили правильно на все вопросы повторения.",
            points = 50
        )
    )
}
