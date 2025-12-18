package com.example.englishlearningapp.ui.topics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.WordEntity
import com.example.englishlearningapp.data.repository.WordRepository
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import android.widget.ImageView

class WordCardFragment : Fragment() {

    // все view
    private lateinit var tvWordIndex: TextView
    private lateinit var btnClose: TextView
    private lateinit var btnFavorite: ImageView

    private lateinit var cardWord: MaterialCardView
    private lateinit var ivStatusEmoji: TextView
    private lateinit var tvWord: TextView
    private lateinit var tvFlipHint: TextView
    private lateinit var tvTranslation: TextView
    private lateinit var tvExample: TextView
    private lateinit var btnPrev: TextView
    private lateinit var btnNext: TextView
    private lateinit var dotsLayout: LinearLayout
    private lateinit var navigationLayout: LinearLayout
    private lateinit var flipButtonsLayout: LinearLayout
    private lateinit var btnDontKnow: TextView
    private lateinit var btnKnow: TextView

    private lateinit var repository: WordRepository
    private var words: List<WordEntity> = emptyList()
    private var currentIndex = 0
    private var topicName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topicName = arguments?.getString("topicName")
        currentIndex = arguments?.getInt("startIndex") ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_word_card, container, false)

        // Инициализация
        tvWordIndex = view.findViewById(R.id.tvWordIndex)
        btnClose = view.findViewById(R.id.btnClose)
        btnFavorite = view.findViewById(R.id.btnFavorite)
        cardWord = view.findViewById(R.id.cardWord)
        ivStatusEmoji = view.findViewById(R.id.ivStatusEmoji)
        tvWord = view.findViewById(R.id.tvWord)
        tvFlipHint = view.findViewById(R.id.tvFlipHint)
        tvTranslation = view.findViewById(R.id.tvTranslation)
        tvExample = view.findViewById(R.id.tvExample)
        btnPrev = view.findViewById(R.id.btnPrev)
        btnNext = view.findViewById(R.id.btnNext)
        dotsLayout = view.findViewById(R.id.dotsLayout)
        navigationLayout = view.findViewById(R.id.navigationLayout)
        flipButtonsLayout = view.findViewById(R.id.flipButtonsLayout)
        btnDontKnow = view.findViewById(R.id.btnDontKnow)
        btnKnow = view.findViewById(R.id.btnKnow)

        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        btnClose.setOnClickListener { parentFragmentManager.popBackStack() }

        btnFavorite.setOnClickListener {
            val word = words.getOrNull(currentIndex) ?: return@setOnClickListener
            val newFavorite = !word.isFavorite
            lifecycleScope.launch {
                repository.setFavorite(word, newFavorite)
                word.isFavorite = newFavorite
                btnFavorite.setImageResource(
                    if (newFavorite) R.drawable.heart_fill else R.drawable.heart
                )
            }
        }
        // Переворот карточки
        cardWord.setOnClickListener {
            val word = words.getOrNull(currentIndex) ?: return@setOnClickListener
            val isFlipped = tvTranslation.visibility == View.VISIBLE

            if (isFlipped) {
                // исходное состояние
                tvWord.visibility = View.VISIBLE
                ivStatusEmoji.visibility = View.VISIBLE
                tvFlipHint.visibility = View.VISIBLE
                tvTranslation.visibility = View.GONE
                tvExample.visibility = View.GONE
                navigationLayout.visibility = View.VISIBLE
                flipButtonsLayout.visibility = View.GONE
                cardWord.setCardBackgroundColor(Color.WHITE)
                cardWord.strokeColor = Color.WHITE
            } else {
                // переворот
                tvWord.visibility = View.GONE
                ivStatusEmoji.visibility = View.GONE
                tvFlipHint.visibility = View.GONE
                tvTranslation.visibility = View.VISIBLE
                tvExample.visibility = View.VISIBLE
                navigationLayout.visibility = View.GONE
                flipButtonsLayout.visibility = View.VISIBLE
                cardWord.setCardBackgroundColor(Color.parseColor("#F5F3FF"))
                cardWord.strokeColor = Color.parseColor("#7C4DFF")
            }
        }

        btnPrev.setOnClickListener { showWord(currentIndex - 1) }
        btnNext.setOnClickListener { showWord(currentIndex + 1) }

        btnDontKnow.setOnClickListener {
            val word = words.getOrNull(currentIndex) ?: return@setOnClickListener
            lifecycleScope.launch {
                repository.setLearned(word, false)
                word.isLearned = false
                showWord(currentIndex + 1)
            }
        }

        btnKnow.setOnClickListener {
            val word = words.getOrNull(currentIndex) ?: return@setOnClickListener
            lifecycleScope.launch {
                repository.setLearned(word, true)
                word.isLearned = true
                showWord(currentIndex + 1)
            }
        }

        loadWords()
        return view
    }

    private fun loadWords() {
        topicName?.let { topic ->
            lifecycleScope.launch {
                words = repository.getWordsByTopic(topic)
                if (words.isNotEmpty()) {
                    currentIndex = currentIndex.coerceIn(0, words.size - 1)
                    setupDots()
                    showWord(currentIndex)
                }
            }
        }
    }

    private fun showWord(index: Int) {
        if (words.isEmpty()) return
        currentIndex = index.coerceIn(0, words.size - 1)
        val word = words[currentIndex]

        tvWord.text = word.word
        ivStatusEmoji.text = word.icon
        tvTranslation.text = word.translation
        tvExample.text = word.example

        tvWord.visibility = View.VISIBLE
        ivStatusEmoji.visibility = View.VISIBLE
        tvFlipHint.visibility = View.VISIBLE
        tvTranslation.visibility = View.GONE
        tvExample.visibility = View.GONE

        navigationLayout.visibility = View.VISIBLE
        flipButtonsLayout.visibility = View.GONE

        cardWord.setCardBackgroundColor(Color.WHITE)
        cardWord.strokeColor = Color.WHITE

        btnFavorite.setImageResource(
            if (word.isFavorite) R.drawable.heart_fill else R.drawable.heart
        )

        tvWordIndex.text = "${currentIndex + 1}/${words.size}"

        updateDots()
    }

    private fun setupDots() {
        dotsLayout.removeAllViews()
        words.forEachIndexed { i, _ ->
            val dot = TextView(requireContext()).apply {
                text = "●"
                textSize = 12f
                setTextColor(if (i == currentIndex) 0xFF7C4DFF.toInt() else 0xFFB0B0B0.toInt())
                setPadding(4, 0, 4, 0)
            }
            dotsLayout.addView(dot)
        }
    }

    private fun updateDots() {
        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i) as TextView
            dot.setTextColor(if (i == currentIndex) 0xFF7C4DFF.toInt() else 0xFFB0B0B0.toInt())
        }
    }
}

