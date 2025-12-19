package com.example.englishlearningapp.ui.tests

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.englishlearningapp.data.model.TestItem

class TestsViewModel : ViewModel() {

    private val _tests = MutableStateFlow<List<TestItem>>(emptyList())
    val tests: StateFlow<List<TestItem>> = _tests

    init {
        _tests.value = listOf(
            TestItem(id = "test_all_words", title = "По всем словам", topic = null, icon = "⭐", questionsCount = 0),
            TestItem(id = "test_favorites", title = "По «избранным» словам", topic = null, icon = "❤️", questionsCount = 0),
            TestItem(id = "test_new", title = "По «новым» словам", topic = null, icon = "🆕", questionsCount = 0),
            TestItem(id = "test_all_topics", title = "По всем темам", topic = null, icon = "📚", questionsCount = 0)
        )
    }
}
