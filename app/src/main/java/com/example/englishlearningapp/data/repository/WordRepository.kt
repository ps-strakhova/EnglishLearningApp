package com.example.englishlearningapp.data.repository

import com.example.englishlearningapp.data.model.Word

class WordRepository {

    fun getWords(): List<Word> {
        return listOf(
            Word(1, "apple", "яблоко", true),
            Word(2, "dog", "собака", true),
            Word(3, "house", "дом", false),
            Word(4, "book", "книга", false)
        )
    }
}
