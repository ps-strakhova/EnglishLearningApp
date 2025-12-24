package com.example.englishlearningapp.data.model

enum class QuestionType {
    OPTIONS,
    INPUT
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String,
    val type: QuestionType,
    val word: WordEntity
)


