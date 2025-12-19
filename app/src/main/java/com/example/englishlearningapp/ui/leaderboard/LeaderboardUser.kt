package com.example.englishlearningapp.ui.leaderboard

data class LeaderboardUser(
    val id: String,
    val name: String,
    val avatar: String,
    val score: Int,
    val words: Int,
    val streak: Int,
    val rank: Int,
    val isCurrentUser: Boolean
)
