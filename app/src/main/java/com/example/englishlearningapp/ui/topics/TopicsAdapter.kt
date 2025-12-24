package com.example.englishlearningapp.ui.topics

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.model.TopicItem
import com.example.englishlearningapp.data.model.SubscriptionManager
import com.example.englishlearningapp.data.model.premiumTopics


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
        val card: View = view
        val icon: TextView = view.findViewById(R.id.iconTopic)
        val title: TextView = view.findViewById(R.id.title)
        val progress: TextView = view.findViewById(R.id.timeAgo)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val item = topics[position]

        val isLocked =
            item.title in premiumTopics && !SubscriptionManager.hasSubscription

        holder.title.text = item.title
        holder.progress.text = "${item.learnedWords} / ${item.totalWords}"

        holder.progressBar.max = item.totalWords
        holder.progressBar.progress = item.learnedWords

        if (isLocked) {
            holder.icon.text = "🔒"
            holder.icon.alpha = 0.5f

            holder.card.alpha = 0.5f
            holder.title.alpha = 0.6f
            holder.progress.alpha = 0.6f
            holder.progressBar.alpha = 0.4f
        } else {
            holder.icon.text = item.iconTopic
            holder.icon.alpha = 1f

            holder.card.alpha = 1f
            holder.title.alpha = 1f
            holder.progress.alpha = 1f
            holder.progressBar.alpha = 1f
        }

        holder.icon.text = item.iconTopic
        holder.icon.alpha = 1f
        holder.itemView.alpha = 1f


        holder.itemView.setOnClickListener {
            onClick(item)
        }


    }


    override fun getItemCount() = topics.size
}
