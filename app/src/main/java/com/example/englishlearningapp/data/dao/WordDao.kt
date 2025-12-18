package com.example.englishlearningapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.englishlearningapp.data.model.WordEntity

@Dao
interface WordDao {

    // =====================
    // ====== READ =========
    // =====================

    @Query("SELECT DISTINCT topic FROM words")
    suspend fun getTopics(): List<String>

    @Query("SELECT * FROM words WHERE topic = :topic")
    suspend fun getWordsByTopic(topic: String): List<WordEntity>

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getTotalWordsCount(): Int

    @Query("SELECT COUNT(*) FROM words WHERE isLearned = 1")
    suspend fun getLearnedWordsCount(): Int

    @Query("SELECT COUNT(*) FROM words WHERE topic = :topic")
    suspend fun getWordsCountByTopic(topic: String): Int

    @Query("SELECT COUNT(*) FROM words WHERE topic = :topic AND isLearned = 1")
    suspend fun getLearnedWordsCountByTopic(topic: String): Int


    // =====================
    // ====== SEED =========
    // =====================

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWordsIgnore(words: List<WordEntity>)

    @Query("""
        UPDATE words
        SET translation = :translation,
            difficulty = :difficulty,
            icon = :icon
        WHERE word = :word AND topic = :topic
    """)
    suspend fun updateWordMeta(
        word: String,
        topic: String,
        translation: String,
        difficulty: String,
        icon: String
    )
}
