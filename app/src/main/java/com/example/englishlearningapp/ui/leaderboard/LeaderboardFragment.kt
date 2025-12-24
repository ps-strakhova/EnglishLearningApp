package com.example.englishlearningapp.ui.leaderboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.repository.WordRepository
import com.example.englishlearningapp.data.model.UserProfilePrefs
import kotlinx.coroutines.launch

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    private lateinit var rvLeaderboard: RecyclerView
    private lateinit var adapter: LeaderboardAdapter
    private lateinit var repository: WordRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvLeaderboard = view.findViewById(R.id.rvLeaderboard)
        adapter = LeaderboardAdapter()
        rvLeaderboard.adapter = adapter
        rvLeaderboard.layoutManager = LinearLayoutManager(requireContext())

        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        val tvCurrentWords: TextView = view.findViewById(R.id.tvCurrentWords)
        val tvPoints: TextView = view.findViewById(R.id.tvPoints)

        lifecycleScope.launch {
            val learnedCount = repository.getLearnedWordsCount()
            val points = UserProfilePrefs.getPoints(requireContext())

            tvCurrentWords.text = learnedCount.toString()
            tvPoints.text = points.toString()
        }

        loadLeaderboard()
    }



    private fun loadLeaderboard() {

        lifecycleScope.launch {
            val userPoints = UserProfilePrefs.getPoints(requireContext())
            val learnedWords = repository.getLearnedWordsCount()

            adapter.submitList(mockLeaderboard(userPoints, learnedWords))
        }
    }

    private fun mockLeaderboard(
        userPoints: Int,
        learnedWords: Int
    ): List<LeaderboardUser> {
        return listOf(
            LeaderboardUser("1", "Maria K.", "👩‍🦰", 2450, 892, 45, 1, false),
            LeaderboardUser("2", "Alex Chen", "👨‍💼", 2380, 856, 38, 2, false),
            LeaderboardUser("3", "Sarah M.", "👱‍♀️", 2210, 798, 32, 3, false),

            // 👇 РЕАЛЬНЫЕ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ
            LeaderboardUser(
                "me",
                "You",
                "😊",
                userPoints,        // ← реальные очки
                learnedWords, // ← реальные выученные слова
                5,
                4,
                true
            )
        )
    }
}


