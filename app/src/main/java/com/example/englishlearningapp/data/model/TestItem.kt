package com.example.englishlearningapp.data.model

data class TestItem(
    val id: String,
    val title: String,
    val topic: String?, // null = общий тест
    val icon: String,
    val questionsCount: Int
)