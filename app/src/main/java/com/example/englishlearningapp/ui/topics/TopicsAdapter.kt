package com.example.englishlearningapp.ui.topics

import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.model.TopicItem

class TopicAdapter(
    private val topics: List<TopicItem>,
    private val onClick: (TopicItem) -> Unit
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topics, parent, false)
        return TopicViewHolder(view)
    }

    inner class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.iconTopic)
        val title: TextView = view.findViewById(R.id.title)
        val progress: TextView = view.findViewById(R.id.timeAgo)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar) // <- прогрессбар
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val item = topics[position]
        holder.icon.text = item.iconTopic
        holder.title.text = item.title
        holder.progress.text = "${item.learnedWords} / ${item.totalWords}"

        // Установка прогресса
        holder.progressBar.max = item.totalWords
        holder.progressBar.progress = item.learnedWords

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }


    override fun getItemCount() = topics.size
}
