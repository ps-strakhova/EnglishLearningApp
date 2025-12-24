package com.example.englishlearningapp.ui.tests

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.SubscriptionManager
import com.example.englishlearningapp.data.model.TestItem
import com.example.englishlearningapp.data.model.allTopics
import com.example.englishlearningapp.data.model.premiumTopics
import kotlinx.coroutines.launch

class TestsFragment : Fragment(R.layout.fragment_tests) {

    private lateinit var adapter: TestsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.testsRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = TestsAdapter { testItem ->
            val isLocked =
                testItem.topic in premiumTopics && !SubscriptionManager.hasSubscription

            if (isLocked) {
                showSubscriptionDialog()
            } else {
                openTest(testItem)
            }
        }

        recycler.adapter = adapter

        loadTests()
    }

    private fun loadTests() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(requireContext()).wordDao()
            val tests = mutableListOf<TestItem>()

            // ---------- ОБЩИЕ ТЕСТЫ (ВСЕГДА ДОСТУПНЫ) ----------
            tests.add(
                TestItem(
                    id = "all_words",
                    title = "По всем словам",
                    topic = null,
                    icon = "📚",
                    questionsCount = dao.getTotalWordsCount()
                )
            )

            tests.add(
                TestItem(
                    id = "favorite_words",
                    title = "По «избранным» словам",
                    topic = null,
                    icon = "❤️",
                    questionsCount = dao.getFavoriteWords().size
                )
            )

            tests.add(
                TestItem(
                    id = "new_words",
                    title = "По «новым» словам",
                    topic = null,
                    icon = "🆕",
                    questionsCount = dao.getUnknownWords().size
                )
            )

            // ---------- ТЕСТЫ ПО ВСЕМ ТЕМАМ (ДАЖЕ ЕСЛИ СЛОВ 0) ----------
            allTopics.forEach { topicConfig ->
                val count = dao.getWordsCountByTopic(topicConfig.title)

                tests.add(
                    TestItem(
                        id = "topic_${topicConfig.title}",
                        title = "По теме: ${topicConfig.title}",
                        topic = topicConfig.title,
                        icon = topicConfig.icon,
                        questionsCount = count
                    )
                )
            }

            // ---------- СОРТИРОВКА: 🔓 СВЕРХУ, 🔒 СНИЗУ ----------
            val sortedTests = tests.sortedBy { test ->
                test.topic in premiumTopics && !SubscriptionManager.hasSubscription
            }

            adapter.submitList(sortedTests)
        }
    }

    private fun openTest(testItem: TestItem) {
        val bundle = Bundle().apply {
            putString("testId", testItem.id)
            putString("testTopic", testItem.topic)
            putString("source", "tests")
        }

        findNavController().navigate(
            R.id.action_testsFragment_to_testRunFragment,
            bundle
        )
    }

    private fun showSubscriptionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Подписка")
            .setMessage("Для доступа к этому тесту необходимо приобрести подписку")
            .setPositiveButton("Понятно", null)
            .show()
    }
}
