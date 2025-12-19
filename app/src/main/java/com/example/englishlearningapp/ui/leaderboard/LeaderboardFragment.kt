package com.example.englishlearningapp.ui.leaderboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    private lateinit var rvLeaderboard: RecyclerView
    private lateinit var adapter: LeaderboardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvLeaderboard = view.findViewById(R.id.rvLeaderboard)
        adapter = LeaderboardAdapter()
        rvLeaderboard.adapter = adapter
        rvLeaderboard.layoutManager = LinearLayoutManager(requireContext())

        adapter.submitList(mockLeaderboard())
    }

    private fun mockLeaderboard(): List<LeaderboardUser> {
        return listOf(
            LeaderboardUser("1", "Maria K.", "👩‍🦰", 2450, 892, 45, 1, false),
            LeaderboardUser("2", "Alex Chen", "👨‍💼", 2380, 856, 38, 2, false),
            LeaderboardUser("3", "Sarah M.", "👱‍♀️", 2210, 798, 32, 3, false),
            LeaderboardUser("me", "You", "😊", 2100, 755, 28, 4, true)
        )
    }
}
