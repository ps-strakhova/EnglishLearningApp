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
            val topics = getSeedTopicsWithCounts(dao)
            recyclerView.adapter = TopicAdapter(topics) { topic ->
                val action = TopicsFragmentDirections.actionTopicsToWords(topic.title)
                findNavController().navigate(action)
            }
        }
    }
}
