package com.example.englishlearningapp.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R

class ProfileFragment : Fragment() {

    // demo data
    private val favoriteWords = listOf(
        Word(1, "Привет", "Hello", "Greetings", Status.FAVORITE),
        Word(2, "Спасибо", "Thank you", "Greetings", Status.FAVORITE),
        Word(3, "Пожалуйста", "Please", "Greetings", Status.FAVORITE),
        Word(4, "Извините", "Excuse me", "Greetings", Status.FAVORITE)
    )

    private val knownWords = listOf(
        Word(11, "Здравствуйте", "Hello (formal)", "Greetings", Status.KNOWN),
        Word(12, "Доброе утро", "Good morning", "Greetings", Status.KNOWN),
        Word(13, "Добрый вечер", "Good evening", "Greetings", Status.KNOWN),
        Word(14, "Спокойной ночи", "Good night", "Greetings", Status.KNOWN),
        Word(15, "До свидания", "Goodbye", "Greetings", Status.KNOWN)
    )

    private val unknownWords = listOf(
        Word(21, "Необходимо", "Necessary", "Advanced", Status.UNKNOWN),
        Word(22, "Возможность", "Opportunity", "Advanced", Status.UNKNOWN),
        Word(23, "Произношение", "Pronunciation", "Advanced", Status.UNKNOWN)
    )

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WordAdapter

    // tabs
    private lateinit var btnFavorites: Button
    private lateinit var btnKnown: Button
    private lateinit var btnUnknown: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Убедись, что это тот layout, где ты положила RecyclerView с id = rvWords
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // header buttons (settings can be ImageButton)
        val btnSettings: ImageButton = view.findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener {
            // placeholder
        }

        val btnCamera: ImageButton = view.findViewById(R.id.btnCamera)
        btnCamera.setOnClickListener {
            // placeholder
        }

        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        btnEdit.setOnClickListener {
            // placeholder
        }

        // stats (static demo)
        val tvWordsLearned: TextView = view.findViewById(R.id.tvWordsLearned)
        val tvStreak: TextView = view.findViewById(R.id.tvStreak)
        val tvRank: TextView = view.findViewById(R.id.tvRank)
        tvWordsLearned.text = "755"
        tvStreak.text = "28"
        tvRank.text = "#4"

        // tabs
        btnFavorites = view.findViewById(R.id.btnFavorites)
        btnKnown = view.findViewById(R.id.btnKnown)
        btnUnknown = view.findViewById(R.id.btnUnknown)

        // RecyclerView — убедись, что id совпадает с layout (в XML в ответах ранее был rvWords)
        recycler = view.findViewById(R.id.rvWords)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter = WordAdapter(listOf())
        recycler.adapter = adapter

        // default tab
        selectTab(Tab.FAVORITES)

        btnFavorites.setOnClickListener { selectTab(Tab.FAVORITES) }
        btnKnown.setOnClickListener { selectTab(Tab.KNOWN) }
        btnUnknown.setOnClickListener { selectTab(Tab.UNKNOWN) }
    }

    private fun selectTab(tab: Tab) {
        // ------------------------
        // 1) Устанавливаем isSelected — чтобы работал selector в drawable (background)
        // ------------------------
        btnFavorites.isSelected = (tab == Tab.FAVORITES)
        btnKnown.isSelected = (tab == Tab.KNOWN)
        btnUnknown.isSelected = (tab == Tab.UNKNOWN)

        // ------------------------
        // 2) Меняем textColor программно (если ты не сделала color selector для текста)
        //    Это нужно, потому что drawable меняет только фон, а текст может оставаться старым.
        // ------------------------
        btnFavorites.setTextColor(
            if (tab == Tab.FAVORITES) Color.parseColor("#FFFFFF") else Color.parseColor("#7C4DFF")
        )
        btnKnown.setTextColor(
            if (tab == Tab.KNOWN) Color.parseColor("#FFFFFF") else Color.parseColor("#4CAF50")
        )
        btnUnknown.setTextColor(
            if (tab == Tab.UNKNOWN) Color.parseColor("#FFFFFF") else Color.parseColor("#F44336")
        )

        // ------------------------
        // 3) Подставляем данные в адаптер (мок-данные пока)
        // ------------------------
        val list = when (tab) {
            Tab.FAVORITES -> favoriteWords
            Tab.KNOWN -> knownWords
            Tab.UNKNOWN -> unknownWords
        }
        adapter.submitList(list)
    }

    // ---------------- models ----------------
    private enum class Tab { FAVORITES, KNOWN, UNKNOWN }
    private enum class Status { FAVORITE, KNOWN, UNKNOWN }

    private data class Word(
        val id: Int,
        val word: String,
        val translation: String,
        val topic: String,
        val status: Status
    )

    // ---------------- adapter ----------------
    private inner class WordAdapter(private var items: List<Word>) : RecyclerView.Adapter<WordAdapter.WH>() {

        inner class WH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvWord: TextView = itemView.findViewById(R.id.tvWord)
            val tvTranslation: TextView = itemView.findViewById(R.id.tvTranslation)
            val tvTopic: TextView = itemView.findViewById(R.id.tvTopic)
            val ivStatus: TextView = itemView.findViewById(R.id.ivStatusEmoji)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
            return WH(v)
        }

        override fun onBindViewHolder(holder: WH, position: Int) {
            val w = items[position]
            holder.tvWord.text = w.word
            holder.tvTranslation.text = w.translation
            holder.tvTopic.text = w.topic

            holder.ivStatus.text = when (w.status) {
                Status.FAVORITE -> "♥"
                Status.KNOWN -> "✔"
                Status.UNKNOWN -> "✖"
            }

            val emojiColor = when (w.status) {
                Status.FAVORITE -> Color.parseColor("#FF9800")
                Status.KNOWN -> Color.parseColor("#4CAF50")
                Status.UNKNOWN -> Color.parseColor("#F44336")
            }
            holder.ivStatus.setTextColor(emojiColor)
        }

        override fun getItemCount(): Int = items.size

        // Можно заменить позже на DiffUtil, но пока для теста так удобно.
        fun submitList(list: List<Word>) {
            this.items = list
            notifyDataSetChanged()
        }
    }
}
