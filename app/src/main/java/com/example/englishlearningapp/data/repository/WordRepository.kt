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

    suspend fun getLearnedWordsCount(): Int {
        return dao.getLearnedWordsCount()
    }

    // =====================
    // ====== TOPICS =======
    // =====================
    suspend fun getTopics(): List<TopicItem> {
        return dao.getTopics().map { topic ->
            TopicItem(
                iconTopic = "📚", // позже можно заменить на map
                title = topic,
                totalWords = dao.getWordsCountByTopic(topic),
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
}
