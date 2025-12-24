package com.example.englishlearningapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.englishlearningapp.data.model.WordEntity
import androidx.room.Update


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

    @Query("UPDATE words SET isLearned = 1 WHERE word = :word")
    suspend fun markAsLearned(word: String)

    @Query("UPDATE words SET isLearned = 0 WHERE word = :word")
    suspend fun removeFromUnknown(word: String)


    // =====================
    // ====== SEED =========
    // =====================

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWordsIgnore(words: List<WordEntity>)

    @Query("""
    UPDATE words
    SET translation = :translation,
        difficulty = :difficulty,
        icon = :icon,
        example = :example
    WHERE word = :word AND topic = :topic
""")
    suspend fun updateWordMeta(
        word: String,
        topic: String,
        translation: String,
        difficulty: String,
        icon: String,
        example: String
    )


    @Query("UPDATE words SET isLearned = :isLearned WHERE id = :id")
    suspend fun updateLearned(id: Int, isLearned: Boolean)

    @Query("SELECT * FROM words WHERE isFavorite = :fav")
    suspend fun getWordsByFavorite(fav: Boolean): List<WordEntity>

    @Query("SELECT * FROM words WHERE isLearned = :learned")
    suspend fun getWordsByLearned(learned: Boolean): List<WordEntity>

    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<WordEntity>

    @Query("SELECT * FROM words WHERE isFavorite = 1")
    suspend fun getFavoriteWords(): List<WordEntity>

    @Query("UPDATE words SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT * FROM words WHERE isLearned = 0")
    suspend fun getUnknownWords(): List<WordEntity>

    @Query("SELECT * FROM words WHERE word = :text LIMIT 1")
    suspend fun getWordByText(text: String): WordEntity?

    @Update
    suspend fun update(word: WordEntity)

    @Query("SELECT * FROM words WHERE id = :id LIMIT 1")
    suspend fun getWordById(id: Int): WordEntity?
}
