package com.example.englishlearningapp.ui.tests

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.Question
import com.example.englishlearningapp.data.model.WordEntity
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

    private var currentQuestionIndex = 0
    private var isAnswered = false
    private var correctAnswersCount = 0

    private var testId: String? = null
    private var testTopic: String? = null
    private var questions: List<Question> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textQuestion = view.findViewById(R.id.textQuestion)
        optionsContainer = view.findViewById(R.id.optionsContainer)
        btnNext = view.findViewById(R.id.btnNext)
        textProgress = view.findViewById(R.id.textProgress)

        btnNext.setOnClickListener { goToNextQuestion() }

        arguments?.let { bundle ->
            testId = bundle.getString("testId")
            testTopic = bundle.getString("testTopic")
        }

        lifecycleScope.launch { loadQuestions() }

        view.findViewById<TextView>(R.id.btnClose)?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private suspend fun loadQuestions() {
        withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).wordDao()

            val words: List<WordEntity> = when {
                testTopic != null -> dao.getWordsByTopic(testTopic!!)
                testId == "all_words" -> dao.getTopics().flatMap { dao.getWordsByTopic(it) }
                testId == "favorite_words" -> dao.getWordsByFavorite(true)
                testId == "new_words" -> dao.getWordsByLearned(false)
                else -> emptyList()
            }

            val shuffledWords = words.shuffled()

            // Для вариантов берем слова **только из той же темы**, чтобы тест был корректным
            val allWordsForOptions = if (testTopic != null) words else dao.getTopics().flatMap { dao.getWordsByTopic(it) }

            questions = shuffledWords.map { word ->
                val options = generateOptions(word, allWordsForOptions)
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
                    correctAnswersCount = 0
                    showQuestion()
                }
            }
        }
    }

    private fun generateOptions(word: WordEntity, allWords: List<WordEntity>): List<String> {
        val options = mutableSetOf(word.translation)
        // Берем случайные переводы из **той же темы**
        val candidates = allWords.filter { it.translation != word.translation }
        while (options.size < 4 && candidates.isNotEmpty()) {
            val randomWord = candidates[Random.nextInt(candidates.size)]
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
                if (optionText == correctAnswer) correctAnswersCount++
                highlightAnswers(correctAnswer, optionText)
                btnNext.visibility = View.VISIBLE
            }

            optionsContainer.addView(optionView)
        }
    }

    private fun highlightAnswers(correctAnswer: String, selectedAnswer: String) {
        for (i in 0 until optionsContainer.childCount) {
            val option = optionsContainer.getChildAt(i) as TextView
            option.isClickable = false
            when {
                option.text == correctAnswer -> option.setBackgroundResource(R.drawable.bg_option_correct)
                option.text == selectedAnswer -> option.setBackgroundResource(R.drawable.bg_option_wrong)
            }
        }
    }

    private fun goToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            showQuestion()
        } else {
            showResultDialog()
        }
    }

    private fun showResultDialog() {
        val totalQuestions = questions.size
        AlertDialog.Builder(requireContext())
            .setTitle("Результат теста")
            .setMessage("Вы ответили правильно на $correctAnswersCount из $totalQuestions ${questionsWord(totalQuestions)}")
            .setPositiveButton("Пройти заново") { _, _ -> restartTest() }
            .setNegativeButton("Вернуться на главную") { _, _ ->
                findNavController().navigate(R.id.homeFragment)
            }
            .setCancelable(false)
            .show()
    }

    private fun restartTest() {
        currentQuestionIndex = 0
        correctAnswersCount = 0
        showQuestion()
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
