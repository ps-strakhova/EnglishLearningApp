package com.example.englishlearningapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.ActivityItem
import com.example.englishlearningapp.data.repository.WordRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).wordDao()
    private val repository = WordRepository(dao)

    private val _totalWords = MutableLiveData(0)
    val totalWords: LiveData<Int> = _totalWords

    private val _learnedWords = MutableLiveData(0)
    val learnedWords: LiveData<Int> = _learnedWords

    val progressPercent: LiveData<Int> = MediatorLiveData<Int>().apply {
        fun calculate() {
            val total = _totalWords.value ?: 0
            val learned = _learnedWords.value ?: 0
            value = if (total == 0) 0 else learned * 100 / total
        }
        addSource(_totalWords) { calculate() }
        addSource(_learnedWords) { calculate() }
    }

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch {
            _totalWords.value = repository.getTotalWordsCount()
            _learnedWords.value = repository.getLearnedWordsCount()
        }
    }

    // ===== Недавняя активность (demo) =====
    val recentActivities = listOf(
        ActivityItem(
            iconEmoji = "📚",
            title = "Вы выучили 5 новых слов",
            category = "Еда",
            timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000, // 2 часа назад
            description = "Вы успешно выучили новые слова по теме «Еда».",
            points = 10,
            correct = 10,
            total = 10
        ),
        ActivityItem(
            iconEmoji = "✅",
            title = "Дневная цель выполнена",
            category = "Приветствия",
            timestamp = System.currentTimeMillis() - 5 * 60 * 60 * 1000, // 5 часов назад
            description = "Поздравляем! Вы достигли своей дневной цели обучения.",
            points = 100,
            correct = 100,
            total = 100
        ),
        ActivityItem(
            iconEmoji = "⭐",
            title = "Идеальный результат",
            category = "Путешествия",
            timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000, // 1 день назад
            description = "Вы ответили правильно на все вопросы повторения.",
            points = 50,
            correct = 50,
            total = 50
        )
    )

}


