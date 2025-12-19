package com.example.englishlearningapp.ui.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishlearningapp.data.model.TestItem
import com.example.englishlearningapp.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TestsViewModel : ViewModel() {

    private val _tests = MutableStateFlow<List<TestItem>>(emptyList())
    val tests: StateFlow<List<TestItem>> = _tests

    init {
        _tests.value = listOf(
            TestItem(
                id = "test_fruits",
                title = "Тест: Фрукты",
                topic = "Фрукты",
                icon = "🍎",
                questionsCount = 10
            ),
            TestItem(
                id = "test_all",
                title = "Общий тест",
                topic = null,
                icon = "⭐",
                questionsCount = 20
            )
        )
    }
}