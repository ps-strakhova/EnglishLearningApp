package com.example.englishlearningapp.ui.topics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.model.WordEntity

class WordsAdapter(
    private val words: List<WordEntity>,
    private val onWordClick: (WordEntity) -> Unit
) : RecyclerView.Adapter<WordsAdapter.WordViewHolder>() {

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.icon)
        val title: TextView = view.findViewById(R.id.title)
        val category: TextView = view.findViewById(R.id.categoryChip)
        val difficulty: TextView = view.findViewById(R.id.timeAgo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_words, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]

        holder.icon.text = word.icon
        holder.title.text = word.word
        holder.category.text = when {
            word.isFavorite -> "♥ Избранное"
            word.isLearned -> "✔ Выучено"
            else -> "✖ Не выучено"
        }
        holder.difficulty.text = "• ${word.difficulty}"

        holder.itemView.setOnClickListener {
            onWordClick(word)
        }
    }

    override fun getItemCount() = words.size
}
