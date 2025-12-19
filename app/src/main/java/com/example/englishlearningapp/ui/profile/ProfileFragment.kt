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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.WordEntity
import com.example.englishlearningapp.data.repository.WordRepository
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var repository: WordRepository
    private var favoriteWords = listOf<WordEntity>()
    private var knownWords = listOf<WordEntity>()

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WordAdapter

    // tabs
    private lateinit var btnFavorites: Button
    private lateinit var btnKnown: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // header buttons
        val btnSettings: ImageButton = view.findViewById(R.id.btnSettings)
        val btnCamera: ImageButton = view.findViewById(R.id.btnCamera)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)

        btnSettings.setOnClickListener { /* placeholder */ }
        btnCamera.setOnClickListener { /* placeholder */ }
        btnEdit.setOnClickListener { /* placeholder */ }

        // stats (demo)
        val tvWordsLearned: TextView = view.findViewById(R.id.tvWordsLearned)
        val tvStreak: TextView = view.findViewById(R.id.tvStreak)
        val tvRank: TextView = view.findViewById(R.id.tvRank)
        tvWordsLearned.text = "755"
        tvStreak.text = "28"
        tvRank.text = "#4"

        // tabs
        btnFavorites = view.findViewById(R.id.btnFavorites)
        btnKnown = view.findViewById(R.id.btnKnown)

        // RecyclerView
        recycler = view.findViewById(R.id.rvWords)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = WordAdapter(listOf())
        recycler.adapter = adapter

        // repository
        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        // load words from DB
        lifecycleScope.launch {
            favoriteWords = repository.getFavoriteWords()
            knownWords = repository.getLearnedWords()

            // обновляем таб по умолчанию
            selectTab(Tab.FAVORITES)

            // обновляем счетчики на кнопках
            btnFavorites.text = "Favorites (${favoriteWords.size})"
            btnKnown.text = "Known (${knownWords.size})"
        }

        // default tab
        btnFavorites.setOnClickListener { selectTab(Tab.FAVORITES) }
        btnKnown.setOnClickListener { selectTab(Tab.KNOWN) }
    }

    private fun selectTab(tab: Tab) {
        // выделяем кнопку
        btnFavorites.isSelected = (tab == Tab.FAVORITES)
        btnKnown.isSelected = (tab == Tab.KNOWN)

        // цвет текста
        btnFavorites.setTextColor(
            if (tab == Tab.FAVORITES) Color.WHITE else Color.parseColor("#7C4DFF")
        )
        btnKnown.setTextColor(
            if (tab == Tab.KNOWN) Color.WHITE else Color.parseColor("#4CAF50")
        )

        // данные для адаптера
        val list = when (tab) {
            Tab.FAVORITES -> favoriteWords.map { convert(it, Tab.FAVORITES) }
            Tab.KNOWN -> knownWords.map { convert(it, Tab.KNOWN) }
        }

        adapter.submitList(list)
    }


    // конвертация WordEntity -> Word для адаптера
    private fun convert(wordEntity: WordEntity, tab: Tab): Word {
        val status = when (tab) {
            Tab.FAVORITES -> Status.FAVORITE
            Tab.KNOWN -> Status.KNOWN
        }
        return Word(
            id = wordEntity.id,
            word = wordEntity.word,
            translation = wordEntity.translation,
            topic = wordEntity.topic,
            status = status
        )
    }



    // ---------------- models ----------------
    private enum class Tab { FAVORITES, KNOWN }
    private enum class Status { FAVORITE, KNOWN }

    private data class Word(
        val id: Int,
        val word: String,
        val translation: String,
        val topic: String,
        val status: Status
    )

    // ---------------- adapter ----------------
    private inner class WordAdapter(private var items: List<Word>) :
        RecyclerView.Adapter<WordAdapter.WH>() {

        inner class WH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvWord: TextView = itemView.findViewById(R.id.tvWord)
            val tvTranslation: TextView = itemView.findViewById(R.id.tvTranslation)
            val tvTopic: TextView = itemView.findViewById(R.id.tvTopic)
            val ivStatus: TextView = itemView.findViewById(R.id.ivStatusEmoji)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_word, parent, false)
            return WH(v)
        }

        override fun onBindViewHolder(holder: WH, position: Int) {
            val w = items[position]
            holder.tvWord.text = w.word
            holder.tvTranslation.text = w.translation
            holder.tvTopic.text = w.topic

            // отображение статуса
            holder.ivStatus.text = when (w.status) {
                Status.FAVORITE -> "♥"
                Status.KNOWN -> "✔"
            }

            val emojiColor = when (w.status) {
                Status.FAVORITE -> Color.parseColor("#FF9800") // оранжевое сердечко для Favorites
                Status.KNOWN -> Color.parseColor("#4CAF50")   // зеленая галочка для Known
            }

            holder.ivStatus.setTextColor(emojiColor)

            // если слово в Known, не показываем сердечко
            if (w.status == Status.KNOWN) {
                holder.ivStatus.text = "✔"
                holder.ivStatus.setTextColor(Color.parseColor("#4CAF50"))
            }
        }


        override fun getItemCount(): Int = items.size

        fun submitList(list: List<Word>) {
            this.items = list
            notifyDataSetChanged()
        }
    }
}
