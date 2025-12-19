package com.example.englishlearningapp.ui.tests

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.Question
import com.example.englishlearningapp.data.model.WordEntity
import com.example.englishlearningapp.data.model.WrongAnswer
import com.example.englishlearningapp.data.repository.WordRepository
import com.example.englishlearningapp.ui.result.TestResultFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class TestRunFragment : Fragment(R.layout.fragment_test_run) {

    private lateinit var textQuestion: TextView
    private lateinit var optionsContainer: LinearLayout
    private lateinit var btnNext: MaterialButton
    private lateinit var textProgress: TextView

    private val wrongAnswersList = mutableListOf<WrongAnswer>()
    private var currentQuestionIndex = 0
    private var isAnswered = false

    private var testId: String? = null
    private var testTopic: String? = null
    private var questions: List<Question> = emptyList()
    private var correctAnswers = 0
    private var wrongAnswers = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textQuestion = view.findViewById(R.id.textQuestion)
        optionsContainer = view.findViewById(R.id.optionsContainer)
        btnNext = view.findViewById(R.id.btnNext)
        textProgress = view.findViewById(R.id.textProgress)

        btnNext.setOnClickListener { goToNextQuestion() }

        testId = arguments?.getString("testId")
        testTopic = arguments?.getString("testTopic")

        lifecycleScope.launch { loadQuestions() }

        view.findViewById<TextView>(R.id.btnClose)?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private suspend fun loadQuestions() {
        withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).wordDao()
            val repository = WordRepository(dao)

            // Берём все слова из базы для генерации вариантов
            val allWords = repository.getAllWords()

            // Слова для текущего теста
            val words: List<WordEntity> = when (testId) {
                "all_words" -> allWords
                "favorite_words" -> repository.getFavoriteWords()
                "new_words" -> repository.getNewWords()
                else -> testTopic?.let { repository.getWordsByTopic(it) } ?: emptyList()
            }

            val shuffledWords = words.shuffled()

            questions = shuffledWords.map { word ->
                // Для тестов по теме используем слова только из этой темы, иначе – все слова
                val optionsSource = if (testTopic != null) {
                    repository.getWordsByTopic(testTopic!!) // только слова темы
                } else {
                    allWords
                }

                val options = generateOptions(word, optionsSource)
                Question(
                    text = "Как переводится слово \"${word.word}\"?",
                    options = options,
                    correctAnswer = word.translation
                )
            }

            withContext(Dispatchers.Main) {
                if (questions.isEmpty()) {
                    textQuestion.text = "Нет вопросов для этого теста"
                    btnNext.visibility = View.GONE
                    textProgress.text = ""
                } else {
                    currentQuestionIndex = 0
                    showQuestion()
                }
            }
        }
    }

    private fun generateOptions(word: WordEntity, sourceWords: List<WordEntity>): List<String> {
        val options = mutableSetOf(word.translation)

        // Берём 3 случайных неправильных перевода из переданного списка
        val candidates = sourceWords.filter { it.translation != word.translation }
        while (options.size < 4 && candidates.isNotEmpty()) {
            val randomWord = candidates.random()
            options.add(randomWord.translation)
        }

        return options.shuffled()
    }

    private fun showQuestion() {
        val question = questions[currentQuestionIndex]
        textQuestion.text = question.text

        val totalQuestions = questions.size
        val currentNumber = currentQuestionIndex + 1
        textProgress.text = "$currentNumber ${questionsWord(currentNumber)} из $totalQuestions ${questionsWord(totalQuestions)}"

        btnNext.text = if (currentQuestionIndex == questions.size - 1) "Завершить" else "Далее"

        showOptions(question.options, question.correctAnswer)
    }

    private fun showOptions(options: List<String>, correctAnswer: String) {
        optionsContainer.removeAllViews()
        isAnswered = false
        btnNext.visibility = View.GONE

        options.forEach { optionText ->
            val optionView = layoutInflater.inflate(R.layout.item_option, optionsContainer, false) as TextView
            optionView.text = optionText
            optionView.setBackgroundResource(R.drawable.bg_option_default)

            optionView.setOnClickListener {
                if (isAnswered) return@setOnClickListener
                isAnswered = true
                highlightAnswers(correctAnswer, optionText)
                btnNext.visibility = View.VISIBLE
            }

            optionsContainer.addView(optionView)
        }
    }

    private fun highlightAnswers(correctAnswer: String, selectedAnswer: String) {
        val question = questions[currentQuestionIndex]
        val isCorrect = correctAnswer == selectedAnswer

        if (isCorrect) correctAnswers++ else {
            wrongAnswers++
            wrongAnswersList.add(
                WrongAnswer(
                    word = extractWordFromQuestion(question.text),
                    translation = correctAnswer,
                    topic = testTopic
                )
            )
        }

        for (i in 0 until optionsContainer.childCount) {
            val option = optionsContainer.getChildAt(i) as TextView
            option.isClickable = false
            when {
                option.text == correctAnswer -> option.setBackgroundResource(R.drawable.bg_option_correct)
                option.text == selectedAnswer -> option.setBackgroundResource(R.drawable.bg_option_wrong)
            }
        }
    }

    private fun extractWordFromQuestion(text: String): String {
        return text.substringAfter("\"").substringBefore("\"")
    }

    private fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showQuestion()
        } else {
            openResultScreen()
        }
    }

    private fun openResultScreen() {
        val fragment = TestResultFragment().apply {
            arguments = Bundle().apply {
                putInt("correct", correctAnswers)
                putInt("wrong", wrongAnswers)
                putSerializable("wrongWords", ArrayList(wrongAnswersList))
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun questionsWord(count: Int): String {
        val rem100 = count % 100
        val rem10 = count % 10
        return when {
            rem100 in 11..19 -> "вопросов"
            rem10 == 1 -> "вопрос"
            rem10 in 2..4 -> "вопроса"
            else -> "вопросов"
        }
    }
}
