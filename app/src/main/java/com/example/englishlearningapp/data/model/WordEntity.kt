package com.example.englishlearningapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val word: String,
    val translation: String,

    val isLearned: Boolean = false,
    val isFavorite: Boolean = false,
    val difficulty: String, // easy / medium / hard
    val topic: String
)
