package com.example.englishlearningapp.ui.topics

import com.example.englishlearningapp.data.getSeedTopicsWithCounts
import com.example.englishlearningapp.data.seedDatabaseIfEmpty
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import kotlinx.coroutines.launch
import com.example.englishlearningapp.data.model.premiumTopics
import com.example.englishlearningapp.data.model.SubscriptionManager
import androidx.appcompat.app.AlertDialog
import com.example.englishlearningapp.data.model.allTopics
import com.example.englishlearningapp.data.model.TopicItem



class TopicsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_topics, container, false)

        recyclerView = view.findViewById(R.id.topicsRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadTopics()


        return view
    }

    private fun loadTopics() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(requireContext()).wordDao()
            val counts = getSeedTopicsWithCounts(dao)

            val topics = allTopics.map { config ->
                val fromDb = counts.find { it.title == config.title }

                TopicItem(
                    title = config.title,
                    iconTopic = config.icon,
                    totalWords = fromDb?.totalWords ?: 0,
                    learnedWords = fromDb?.learnedWords ?: 0
                )
            }


            val sortedTopics = topics.sortedBy { topic ->
                topic.title in premiumTopics && !SubscriptionManager.hasSubscription
            }

            recyclerView.adapter = TopicAdapter(sortedTopics) { topic ->
                val isLocked =
                    topic.title in premiumTopics && !SubscriptionManager.hasSubscription

                if (isLocked) {
                    showSubscriptionDialog()
                } else {
                    val action =
                        TopicsFragmentDirections.actionTopicsToWords(topic.title)
                    findNavController().navigate(action)
                }
            }


        }
    }

    private fun showSubscriptionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Подписка")
            .setMessage("Для доступа к этой теме необходимо приобрести подписку в профиле")
            .setPositiveButton("Понятно", null)
            .show()
    }

}
