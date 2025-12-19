package com.example.englishlearningapp.ui.tests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.model.TestItem

class TestsAdapter(
    private val onClick: (TestItem) -> Unit
) : RecyclerView.Adapter<TestsAdapter.VH>() {

    private var items: List<TestItem> = emptyList()

    fun submitList(newItems: List<TestItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: TextView = itemView.findViewById(R.id.icon)
        val title: TextView = itemView.findViewById(R.id.title)
        val subtitle: TextView = itemView.findViewById(R.id.subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test, parent, false)
        return VH(view)
    }

    fun questionsWord(count: Int): String {
        val rem100 = count % 100
        val rem10 = count % 10

        return when {
            rem100 in 11..19 -> "вопросов"
            rem10 == 1 -> "вопрос"
            rem10 in 2..4 -> "вопроса"
            else -> "вопросов"
        }
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.icon.text = item.icon
        holder.title.text = item.title
        holder.subtitle.text = "${item.questionsCount} ${questionsWord(item.questionsCount)}"

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
