package com.example.englishlearningapp.ui.tests

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.TestItem
import com.example.englishlearningapp.data.repository.WordRepository
import kotlinx.coroutines.launch

class TestsFragment : Fragment(R.layout.fragment_tests) {

    private lateinit var adapter: TestsAdapter
    private lateinit var repository: WordRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        val recycler = view.findViewById<RecyclerView>(R.id.testsRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = TestsAdapter { testItem -> openTest(testItem) }
        recycler.adapter = adapter

        lifecycleScope.launch {
            val tests = mutableListOf<TestItem>()

            // Общие тесты
            tests.add(
                TestItem(
                    id = "all_words",
                    title = "По всем словам",
                    topic = null,
                    icon = "📚",
                    questionsCount = repository.getTotalWordsCount()
                )
            )
            tests.add(
                TestItem(
                    id = "favorite_words",
                    title = "По «избранным» словам",
                    topic = null,
                    icon = "❤️",
                    questionsCount = repository.getFavoriteWords().size
                )
            )
            tests.add(
                TestItem(
                    id = "new_words",
                    title = "По «новым» словам",
                    topic = null,
                    icon = "🆕",
                    questionsCount = repository.getUnknownWords().size
                )
            )

            // Тесты по существующим темам с эмоджи из базы
            val topics = repository.getTopics()
            topics.forEach { topicItem ->
                tests.add(
                    TestItem(
                        id = "topic_${topicItem.title}",
                        title = "По теме: ${topicItem.title}",
                        topic = topicItem.title,
                        icon = topicItem.iconTopic, // иконка теперь реально из темы
                        questionsCount = topicItem.totalWords
                    )
                )
            }

            adapter.submitList(tests)
        }
    }

    private fun openTest(testItem: TestItem) {
        val fragment = TestRunFragment()
        fragment.arguments = Bundle().apply {
            putString("testId", testItem.id)
            putString("testTitle", testItem.title)
            putString("testTopic", testItem.topic)
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}
