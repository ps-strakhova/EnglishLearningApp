package com.example.englishlearningapp.data.model

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)
