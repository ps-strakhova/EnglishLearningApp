package com.example.englishlearningapp.data.repository
import com.example.englishlearningapp.data.dao.WordDao
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.WordEntity
import com.example.englishlearningapp.data.TopicIcons
import com.example.englishlearningapp.data.model.TestItem



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

    suspend fun getTests(): List<TestItem> {
        val topics = dao.getTopics()

        val topicTests = topics.map { topic ->
            val count = dao.getWordsCountByTopic(topic)

            TestItem(
                id = "test_$topic",
                title = "Тест: $topic",
                topic = topic,
                icon = TopicIcons.getIcon(topic),
                questionsCount = count.coerceAtMost(10)
            )
        }

        val totalQuestions = dao.getTotalWordsCount()

        val allTest = TestItem(
            id = "test_all",
            title = "Общий тест",
            topic = null,
            icon = TopicIcons.getIcon(null),
            questionsCount = totalQuestions.coerceAtMost(20)
        )

        return topicTests + allTest
    }

}
