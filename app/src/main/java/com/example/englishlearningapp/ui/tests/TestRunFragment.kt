package com.example.englishlearningapp.ui.tests

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.*
import com.example.englishlearningapp.data.repository.WordRepository
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestRunFragment : Fragment(R.layout.fragment_test_run) {

    private lateinit var repository: WordRepository

    private lateinit var textQuestion: TextView
    private lateinit var optionsContainer: LinearLayout
    private lateinit var btnNext: MaterialButton
    private lateinit var textProgress: TextView

    private val wrongAnswers = mutableListOf<WrongAnswer>()

    private var index = 0
    private var correctCount = 0
    private var isAnswered = false

    private var questions: List<Question> = emptyList()

    private var source: String? = null
    private var testTopic: String? = null
    private var topicIcon: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = WordRepository(
            AppDatabase.getDatabase(requireContext()).wordDao()
        )

        textQuestion = view.findViewById(R.id.textQuestion)
        optionsContainer = view.findViewById(R.id.optionsContainer)
        btnNext = view.findViewById(R.id.btnNext)
        textProgress = view.findViewById(R.id.textProgress)

        source = arguments?.getString("source")
        testTopic = arguments?.getString("testTopic")
        topicIcon = arguments?.getString("topicIcon")

        btnNext.setOnClickListener { nextQuestion() }

        view.findViewById<TextView>(R.id.btnClose).setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch { loadQuestions() }
    }

    private suspend fun loadQuestions() = withContext(Dispatchers.IO) {
        val allWords = repository.getAllWords()

        val words = when (arguments?.getString("testId")) {
            "quick_random" -> allWords.shuffled().take(10)
            "favorite_words" -> repository.getFavoriteWords()
            "new_words" -> repository.getNewWords()
            else -> testTopic?.let { repository.getWordsByTopic(it) } ?: emptyList()
        }

        questions = words.shuffled().map { word ->
            val type =
                if ((0..100).random() < 30) QuestionType.INPUT else QuestionType.OPTIONS

            if (type == QuestionType.INPUT) {
                Question(
                    word = word,
                    text = "Как пишется слово «${word.translation}» на английском?",
                    options = emptyList(),
                    correctAnswer = word.word,
                    type = type
                )
            } else {
                Question(
                    word = word,
                    text = "Как переводится слово \"${word.word}\"?",
                    options = generateOptions(word, allWords),
                    correctAnswer = word.translation,
                    type = type
                )
            }
        }

        withContext(Dispatchers.Main) { showQuestion() }
    }

    private fun generateOptions(word: WordEntity, all: List<WordEntity>): List<String> {
        val options = mutableSetOf(word.translation)
        val pool = all.filter { it.translation != word.translation }
        while (options.size < 4) options.add(pool.random().translation)
        return options.shuffled()
    }

    private fun showQuestion() {
        val q = questions[index]

        textQuestion.text = q.text
        textProgress.text = "${index + 1} из ${questions.size}"
        btnNext.visibility = View.GONE
        isAnswered = false
        optionsContainer.removeAllViews()

        if (q.type == QuestionType.OPTIONS) showOptions(q)
        else showInput(q)
    }

    private fun showOptions(q: Question) {
        q.options.forEach { option ->
            val tv =
                layoutInflater.inflate(R.layout.item_option, optionsContainer, false) as TextView
            tv.text = option

            tv.setOnClickListener {
                if (isAnswered) return@setOnClickListener
                isAnswered = true

                if (option == q.correctAnswer) {
                    correctCount++
                    tv.setBackgroundResource(R.drawable.bg_option_correct)
                } else {
                    wrongAnswers.add(
                        WrongAnswer(
                            word = q.word.word,
                            translation = q.word.translation,
                            topic = q.word.topic,
                            wordId = q.word.id
                        )
                    )
                    tv.setBackgroundResource(R.drawable.bg_option_wrong)
                    highlightCorrect(q.correctAnswer)
                }

                btnNext.visibility = View.VISIBLE
            }
            optionsContainer.addView(tv)
        }
    }

    private fun highlightCorrect(correct: String) {
        for (i in 0 until optionsContainer.childCount) {
            val v = optionsContainer.getChildAt(i) as TextView
            if (v.text == correct) {
                v.setBackgroundResource(R.drawable.bg_option_correct)
            }
        }
    }

    private fun showInput(q: Question) {
        val v = layoutInflater.inflate(R.layout.item_input_answer, optionsContainer, false)

        val input = v.findViewById<TextView>(R.id.etAnswer)
        val btnCheck = v.findViewById<MaterialButton>(R.id.btnCheck)
        val tvCorrect = v.findViewById<TextView>(R.id.tvCorrectAnswer)

        btnCheck.setOnClickListener {
            if (isAnswered) return@setOnClickListener
            isAnswered = true

            val userAnswer = input.text.toString().trim()

            if (userAnswer.equals(q.correctAnswer, true)) {
                correctCount++
                input.setBackgroundResource(R.drawable.bg_input_correct)
            } else {
                wrongAnswers.add(
                    WrongAnswer(
                        word = q.word.word,
                        translation = q.word.translation,
                        topic = q.word.topic,
                        wordId = q.word.id
                    )
                )

                input.setBackgroundResource(R.drawable.bg_input_wrong)

                tvCorrect.text = "Правильный ответ: ${q.correctAnswer}"
                tvCorrect.visibility = View.VISIBLE
            }

            btnCheck.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
        }

        optionsContainer.addView(v)
    }


    private fun nextQuestion() {
        if (index < questions.lastIndex) {
            index++
            showQuestion()
        } else finishTest()
    }

    private fun finishTest() {
        findNavController().navigate(
            R.id.action_testRunFragment_to_testResultFragment,
            Bundle().apply {
                putInt("correct", correctCount)
                putInt("wrong", wrongAnswers.size)
                putSerializable("wrongWords", ArrayList(wrongAnswers))
                putString("source", source)
                putString("testTopic", testTopic)
                putString("topicIcon", topicIcon)
            }
        )
    }
}
