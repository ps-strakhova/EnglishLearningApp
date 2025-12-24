package com.example.englishlearningapp.data.model

import java.io.Serializable

data class WrongAnswer(
    val word: String,
    val translation: String,
    val topic: String,
    val wordId: Int            // 👈 ВАЖНО
) : Serializable

