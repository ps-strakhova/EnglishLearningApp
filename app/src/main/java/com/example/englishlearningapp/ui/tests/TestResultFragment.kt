package com.example.englishlearningapp.ui.result

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.WordEntity
import com.example.englishlearningapp.data.model.WrongAnswer
import com.example.englishlearningapp.data.repository.WordRepository
import com.example.englishlearningapp.ui.tests.TestsFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class TestResultFragment : Fragment(R.layout.fragment_test_result) {

    private lateinit var repository: WordRepository
    private lateinit var testResultChart: PieChart
    private lateinit var errorsContainer: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        testResultChart = view.findViewById(R.id.testResultChart)
        errorsContainer = view.findViewById(R.id.errorsContainer)

        val correct = arguments?.getInt("correct") ?: 0
        val wrong = arguments?.getInt("wrong") ?: 0
        val wrongWords = arguments?.getSerializable("wrongWords") as? List<WrongAnswer> ?: emptyList()

        setupResultChart(correct, wrong)
        showErrors(wrongWords)

        // Крестик закрытия
        view.findViewById<TextView>(R.id.btnClose).setOnClickListener {
            openTestsFragment()
        }

        // Кнопка "Вернуться на главный экран"
        view.findViewById<MaterialButton>(R.id.btnBackToMain).setOnClickListener {
            openTestsFragment()
        }
    }

    private fun openTestsFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, TestsFragment())
            .commit()
    }

    private fun showErrors(errors: List<WrongAnswer>) {
        errorsContainer.removeAllViews()

        if (errors.isEmpty()) {
            val tv = TextView(requireContext()).apply {
                text = "Ошибок нет 🎉"
                textSize = 14f
                setTextColor(Color.GRAY)
            }
            errorsContainer.addView(tv)
            return
        }

        errors.forEach { error ->
            val view = layoutInflater.inflate(
                R.layout.item_test_incorrect_word,
                errorsContainer,
                false
            )

            view.findViewById<TextView>(R.id.ivStatusEmoji).text = "❌"
            view.findViewById<TextView>(R.id.tvWord).text = error.word
            view.findViewById<TextView>(R.id.tvTranslation).text = error.translation
            view.findViewById<TextView>(R.id.tvTopic).text = error.topic ?: "Общий"

            // Кнопка "+" добавления в новые слова
            val btnAdd = view.findViewById<TextView>(R.id.tvAddToUnknown)
            btnAdd.setOnClickListener {
                lifecycleScope.launch {
                    val newWord = WordEntity(
                        word = error.word,
                        translation = error.translation,
                        topic = error.topic ?: "Общий",
                        isLearned = false, // ключевой параметр — добавляем как новые
                        isFavorite = false,
                        difficulty = "medium",
                        icon = "🆕",
                        example = ""
                    )
                    repository.seedOrUpdate(listOf(newWord)) // добавляем в базу

                    // визуальная обратная связь
                    btnAdd.text = "✓"
                    btnAdd.isEnabled = false
                }
            }

            errorsContainer.addView(view)
        }
    }

    private fun setupResultChart(correct: Int, wrong: Int) {
        val percent = if (correct + wrong > 0) correct * 100 / (correct + wrong) else 0

        testResultChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)
            isDrawHoleEnabled = true
            holeRadius = 72f
            transparentCircleRadius = 76f
        }

        val entries = listOf(PieEntry(correct.toFloat()), PieEntry(wrong.toFloat()))
        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(Color.parseColor("#6C4DFF"), Color.parseColor("#E9E7FF"))
            setDrawValues(false)
        }

        testResultChart.data = PieData(dataSet)
        testResultChart.animateY(900, Easing.EaseInOutQuad)

        val centerText = SpannableString("$percent%\nверных ответов").apply {
            setSpan(RelativeSizeSpan(2f), 0, "$percent%".length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), 0, "$percent%".length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(Color.parseColor("#6C4DFF")), 0, "$percent%".length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        testResultChart.centerText = centerText
    }
}
