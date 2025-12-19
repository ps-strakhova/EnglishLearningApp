package com.example.englishlearningapp.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R

class LeaderboardAdapter :
    ListAdapter<LeaderboardUser, LeaderboardAdapter.LeaderboardViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LeaderboardUser>() {
            override fun areItemsTheSame(oldItem: LeaderboardUser, newItem: LeaderboardUser) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: LeaderboardUser, newItem: LeaderboardUser) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user = getItem(position)

        holder.tvName.text = user.name
        holder.tvAvatar.text = user.avatar
        holder.tvScore.text = user.score.toString()
        holder.tvStats.text = "${user.words} words • 🔥 ${user.streak} days"

        holder.tvYouBadge.visibility = if (user.isCurrentUser) View.VISIBLE else View.GONE

        when (user.rank) {
            1 -> setCrown(holder, R.drawable.bg_rank_gold)
            2 -> setCrown(holder, R.drawable.bg_rank_silver)
            3 -> setCrown(holder, R.drawable.bg_rank_bronze)
            else -> setRankNumber(holder, user.rank)
        }
    }

    private fun setCrown(holder: LeaderboardViewHolder, bg: Int) {
        holder.rankContainer.setBackgroundResource(bg)
        holder.ivCrown.visibility = View.VISIBLE
        holder.tvRank.visibility = View.GONE
    }

    private fun setRankNumber(holder: LeaderboardViewHolder, rank: Int) {
        holder.rankContainer.setBackgroundResource(R.drawable.bg_rank_default)
        holder.ivCrown.visibility = View.GONE
        holder.tvRank.visibility = View.VISIBLE
        holder.tvRank.text = rank.toString()
    }

    class LeaderboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rankContainer: FrameLayout = view.findViewById(R.id.rankContainer)
        val ivCrown: ImageView = view.findViewById(R.id.ivCrown)
        val tvRank: TextView = view.findViewById(R.id.tvRank)
        val tvAvatar: TextView = view.findViewById(R.id.tvAvatar)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvStats: TextView = view.findViewById(R.id.tvStats)
        val tvScore: TextView = view.findViewById(R.id.tvScore)
        val tvYouBadge: TextView = view.findViewById(R.id.tvYouBadge)
    }
}
