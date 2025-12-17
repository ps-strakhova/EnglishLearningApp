package com.example.englishlearningapp.ui.topics

import com.example.englishlearningapp.data.getSeedTopicsWithCounts
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.englishlearningapp.R
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.repository.WordRepository
import kotlinx.coroutines.launch


class TopicsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: WordRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_topics, container, false)

        recyclerView = view.findViewById(R.id.topicsRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        loadTopics()

        return view
    }

    private fun loadTopics() {
        lifecycleScope.launch {
            val topics = getSeedTopicsWithCounts(AppDatabase.getDatabase(requireContext()).wordDao())
            recyclerView.adapter = TopicAdapter(topics) { topic ->
                // тут переход к словам темы
            }
        }
    }
}
