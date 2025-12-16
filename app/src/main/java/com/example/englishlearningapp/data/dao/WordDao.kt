package com.example.englishlearningapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.englishlearningapp.data.model.WordEntity

@Dao
interface WordDao {

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getTotalWordsCount(): Int

    @Query("SELECT COUNT(*) FROM words WHERE isLearned = 1")
    suspend fun getLearnedWordsCount(): Int
}
