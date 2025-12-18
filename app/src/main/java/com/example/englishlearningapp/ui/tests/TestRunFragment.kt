package com.example.englishlearningapp.ui.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.englishlearningapp.R
import com.google.android.material.button.MaterialButton

class TestRunFragment : Fragment() {

    private lateinit var textQuestion: TextView
    private lateinit var optionsContainer: LinearLayout
    private lateinit var btnNext: MaterialButton

    private var currentQuestionIndex = 0
    private var isAnswered = false

    // ВРЕМЕННЫЕ тестовые данные (позже заменим на реальные из БД)
    private val questions = listOf(
        Question(
            text = "Как переводится слово \"Apple\"?",
            options = listOf("Яблоко", "Банан", "Груша", "Апельсин"),
            correctAnswer = "Яблоко"
        ),
        Question(
            text = "Как переводится слово \"Car\"?",
            options = listOf("Поезд", "Самолёт", "Машина", "Корабль"),
            correctAnswer = "Машина"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_test_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textQuestion = view.findViewById(R.id.textQuestion)
        optionsContainer = view.findViewById(R.id.optionsContainer)
        btnNext = view.findViewById(R.id.btnNext)

        btnNext.visibility = View.GONE

        btnNext.setOnClickListener {
            goToNextQuestion()
        }

        showQuestion()
    }

    // ===== ПОКАЗ ВОПРОСА =====
    private fun showQuestion() {
        val question = questions[currentQuestionIndex]

        textQuestion.text = question.text
        showOptions(question.options, question.correctAnswer)
    }

    // ===== ПОКАЗ ВАРИАНТОВ =====
    private fun showOptions(
        options: List<String>,
        correctAnswer: String
    ) {
        optionsContainer.removeAllViews()
        isAnswered = false
        btnNext.visibility = View.GONE

        options.forEach { optionText ->
            val optionView = layoutInflater.inflate(
                R.layout.item_option,
                optionsContainer,
                false
            ) as TextView

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

    // ===== ПОДСВЕТКА =====
    private fun highlightAnswers(
        correctAnswer: String,
        selectedAnswer: String
    ) {
        for (i in 0 until optionsContainer.childCount) {
            val option = optionsContainer.getChildAt(i) as TextView
            option.isClickable = false

            when {
                option.text == correctAnswer -> {
                    option.setBackgroundResource(R.drawable.bg_option_correct)
                }
                option.text == selectedAnswer -> {
                    option.setBackgroundResource(R.drawable.bg_option_wrong)
                }
            }
        }
    }

    // ===== ПЕРЕХОД ДАЛЬШЕ =====
    private fun goToNextQuestion() {
        currentQuestionIndex++

        if (currentQuestionIndex < questions.size) {
            showQuestion()
        } else {
            showResultScreen()
        }
    }

    // ===== РЕЗУЛЬТАТ (ЗАГЛУШКА) =====
    private fun showResultScreen() {
        textQuestion.text = "Тест завершён 🎉"
        optionsContainer.removeAllViews()
        btnNext.visibility = View.GONE
    }
}

// ===== МОДЕЛЬ ВОПРОСА =====
data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)
