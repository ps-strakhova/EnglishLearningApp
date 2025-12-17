package com.example.englishlearningapp.data.repository
import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity

class WordRepository(private val dao: WordDao) {

    suspend fun getTotalWordsCount(): Int {
        return dao.getTotalWordsCount()
    }

    suspend fun getLearnedWordsCount(): Int {
        return dao.getLearnedWordsCount()
    }

    suspend fun getTopics(): List<TopicItem> {
        return dao.getTopics().map { topic ->
            TopicItem(
                iconTopic = "📚",
                title = topic,
                totalWords = dao.getWordsCountByTopic(topic),
                learnedWords = dao.getLearnedWordsCountByTopic(topic)
            )
        }
    }

    suspend fun getWordsByTopic(topic: String): List<WordEntity> {
        return dao.getWordsByTopic(topic)
    }
}
