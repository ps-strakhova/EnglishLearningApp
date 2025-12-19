package com.example.englishlearningapp.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

data class WordKey(
    val word: String,
    val topic: String
)
