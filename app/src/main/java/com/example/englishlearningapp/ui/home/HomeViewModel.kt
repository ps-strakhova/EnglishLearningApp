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

    private fun loadProgress() {
        viewModelScope.launch {
            _totalWords.value = repository.getTotalWordsCount()
            _learnedWords.value = repository.getLearnedWordsCount()
        }
    }

    // ===== Статистика =====
    val newWordsToday: Int = 20    // вот эти свойства нужны
    val streakDays: Int = 5

    // ===== Недавняя активность =====
    val recentActivities = listOf(
        ActivityItem("📚", "Вы выучили 5 новых слов", "Еда", "2 часа назад",
            "Вы успешно выучили новые слова по теме «Еда».", 100),
        ActivityItem("✅", "Дневная цель выполнена", "Приветствия", "5 часов назад",
            "Поздравляем! Вы достигли своей дневной цели обучения.", 100),
        ActivityItem("⭐", "Идеальный результат", "Путешествия", "1 день назад",
            "Вы ответили правильно на все вопросы повторения.", 50)
    )
}


