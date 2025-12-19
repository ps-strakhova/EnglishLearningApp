package com.example.englishlearningapp.data.model

data class TestQuestion(
    val word: String,
    val correctAnswer: String,
    val options: List<String>? = null // null = ввод ответа
)