package com.example.englishlearningapp.data.repository

import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity

class WordRepository(private val dao: WordDao) {

    // =====================
    // ====== SEED =========
    // =====================
    suspend fun seedOrUpdate(words: List<WordEntity>) {
        // 1. Добавляем новые слова (если их ещё нет)
        dao.insertWordsIgnore(words)

        // 2. Обновляем мета-данные существующих слов
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
    suspend fun getTotalWordsCount(): Int {
        return dao.getTotalWordsCount()
    }

    suspend fun getAllWords(): List<WordEntity> {
        val topics = dao.getTopics()
        val list = mutableListOf<WordEntity>()
        topics.forEach { topic ->
            list.addAll(dao.getWordsByTopic(topic))
        }
        return list
    }


    suspend fun getLearnedWordsCount(): Int {
        return dao.getLearnedWordsCount()
    }

    // =====================
    // ====== TOPICS =======
    // =====================
    suspend fun getTopics(): List<TopicItem> {
        return dao.getTopics().map { topic ->
            val wordsInTopic = dao.getWordsByTopic(topic)
            val icon = wordsInTopic.firstOrNull()?.icon ?: "📚"
            TopicItem(
                iconTopic = icon,
                title = topic,
                totalWords = wordsInTopic.size,
                learnedWords = dao.getLearnedWordsCountByTopic(topic)
            )
        }
    }

    // =====================
    // ====== WORDS ========
    // =====================
    suspend fun getWordsByTopic(topic: String): List<WordEntity> {
        return dao.getWordsByTopic(topic)
    }

    suspend fun setFavorite(word: WordEntity, favorite: Boolean) {
        dao.updateFavorite(word.id, favorite)
    }

    suspend fun setLearned(word: WordEntity, learned: Boolean) {
        dao.updateLearned(word.id, learned)
    }

    suspend fun getFavoriteWords(): List<WordEntity> {
        return dao.getWordsByFavorite(true)
    }

    suspend fun getLearnedWords(): List<WordEntity> {
        return dao.getWordsByLearned(true)
    }

    suspend fun getUnknownWords(): List<WordEntity> {
        return dao.getWordsByLearned(false)
    }

}
