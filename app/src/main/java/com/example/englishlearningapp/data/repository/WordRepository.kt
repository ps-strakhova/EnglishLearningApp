package com.example.englishlearningapp.data.repository

import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity

class WordRepository(private val dao: WordDao) {


    // =====================
    // ====== SEED =========
    // =====================
    suspend fun seedOrUpdate(words: List<WordEntity>) {
        // Добавляем новые слова (если их ещё нет)
        dao.insertWordsIgnore(words)

        // Обновляем мета-данные существующих слов
        words.forEach { word ->
            dao.updateWordMeta(
                word = word.word,
                topic = word.topic,
                translation = word.translation,
                difficulty = word.difficulty,
                icon = word.icon,
                example = word.example
            )
        }
    }

    // =====================
    // ====== STATS ========
    // =====================
    suspend fun getTotalWordsCount(): Int = dao.getTotalWordsCount()

    suspend fun getLearnedWordsCount(): Int = dao.getLearnedWordsCount()

    // =====================
    // ====== TOPICS =======
    // =====================
    suspend fun getTopics(): List<TopicItem> {
        return dao.getTopics().map { topic ->
            val wordsInTopic = dao.getWordsByTopic(topic)
            TopicItem(
                iconTopic = wordsInTopic.firstOrNull()?.icon ?: "📚",
                title = topic,
                totalWords = wordsInTopic.size,
                learnedWords = dao.getLearnedWordsCountByTopic(topic)
            )
        }
    }

    // =====================
    // ====== WORDS ========
    // =====================
    suspend fun getWordsByTopic(topic: String): List<WordEntity> = dao.getWordsByTopic(topic)

    suspend fun setFavorite(word: WordEntity, favorite: Boolean) = dao.updateFavorite(word.id, favorite)
    suspend fun getFavoriteWords(): List<WordEntity> = dao.getFavoriteWords()
    suspend fun setLearned(word: WordEntity, learned: Boolean) = dao.updateLearned(word.id, learned)

    suspend fun getLearnedWords(): List<WordEntity> = dao.getWordsByLearned(true)

    suspend fun getUnknownWords(): List<WordEntity> = dao.getWordsByLearned(false)

    suspend fun getAllWords(): List<WordEntity> = dao.getAllWords() // теперь использует DAO напрямую

    suspend fun getNewWords(): List<WordEntity> = dao.getUnknownWords() // теперь использует DAO напрямую

}
