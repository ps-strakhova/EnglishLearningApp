package com.example.englishlearningapp.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "words",
    indices = [Index(value = ["word", "topic"], unique = true)]
)
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val word: String,
    val translation: String,
    val example: String,
    var isLearned: Boolean = false,
    var isFavorite: Boolean = false,
    val difficulty: String,
    val topic: String,
    val icon: String = "📚"
)
