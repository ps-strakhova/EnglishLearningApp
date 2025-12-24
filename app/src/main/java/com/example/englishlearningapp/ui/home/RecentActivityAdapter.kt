package com.example.englishlearningapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.model.ActivityItem
import com.example.englishlearningapp.data.model.TimeAgoUtils



class RecentActivityAdapter(
    private val items: List<ActivityItem>,
    private val onClick: (ActivityItem) -> Unit
) : RecyclerView.Adapter<RecentActivityAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.icon)
        val title: TextView = view.findViewById(R.id.title)
        val category: TextView = view.findViewById(R.id.categoryChip)
        val timeAgo: TextView = view.findViewById(R.id.timeAgo)
        val points: TextView = view.findViewById(R.id.tvPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_activity, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.icon.text = item.iconEmoji
        holder.title.text = item.title
        holder.category.text = item.category
        holder.timeAgo.text = TimeAgoUtils.getTimeAgo(item.timestamp)
        holder.points.text = "+${item.points}"

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}

