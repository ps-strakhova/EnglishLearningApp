package com.example.englishlearningapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val translation: String,
    val topic: String,
    val difficulty: String,
    val isLearned: Boolean = false,
    val icon: String   // ✅ БЫЛ Int → стал String
)

