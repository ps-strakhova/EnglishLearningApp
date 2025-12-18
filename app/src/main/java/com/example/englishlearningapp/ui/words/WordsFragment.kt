package com.example.englishlearningapp.ui.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

class WordsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: WordRepository

    private lateinit var textWelcome: TextView
    private lateinit var textSubtitle: TextView

    private var topicName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Получаем название темы из аргументов
        topicName = arguments?.getString("topicName")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_words, container, false)

        // Инициализация кнопки назад
        val buttonBack = view.findViewById<ImageView>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack() // вернёт к предыдущему фрагменту
        }

        textWelcome = view.findViewById(R.id.textWelcome)
        textSubtitle = view.findViewById(R.id.textSubtitle)
        recyclerView = view.findViewById(R.id.wordsRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        topicName?.let {
            textWelcome.text = it
        }

        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        loadWords()

        return view
    }

    private fun loadWords() {
        topicName?.let { topic ->
            lifecycleScope.launch {
                val words = repository.getWordsByTopic(topic)
                recyclerView.adapter = WordsAdapter(words)
                textSubtitle.text = "${words.size} карточек"
            }
        }
    }
}
