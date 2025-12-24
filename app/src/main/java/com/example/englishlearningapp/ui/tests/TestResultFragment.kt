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
import androidx.navigation.fragment.findNavController
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.*
import com.example.englishlearningapp.data.repository.WordRepository
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class TestResultFragment : Fragment(R.layout.fragment_test_result) {

    private lateinit var repository: WordRepository
    private lateinit var chart: PieChart
    private lateinit var errorsContainer: LinearLayout



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository =
            WordRepository(AppDatabase.getDatabase(requireContext()).wordDao())

        chart = view.findViewById(R.id.testResultChart)
        errorsContainer = view.findViewById(R.id.errorsContainer)

        val correct = arguments?.getInt("correct") ?: 0
        val wrong = arguments?.getInt("wrong") ?: 0
        val total = correct + wrong


        val errors =
            arguments?.getSerializable("wrongWords") as? List<WrongAnswer> ?: emptyList()

        setupChart(correct, wrong)
        showEarnedPoints(UserProfilePrefs.addPoints(requireContext(), correct * 2))
        saveRecentActivity(correct, total)
        showErrors(errors)

        view.findViewById<TextView>(R.id.btnClose).setOnClickListener { goBack() }
        view.findViewById<MaterialButton>(R.id.btnBackToMain).setOnClickListener { goBack() }

    }

    private fun setupChart(correct: Int, wrong: Int) {
        val total = correct + wrong
        val percent = if (total == 0) 0 else correct * 100 / total

        chart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)

            isDrawHoleEnabled = true
            holeRadius = 72f
            transparentCircleRadius = 76f
        }

        val dataSet = PieDataSet(
            listOf(
                PieEntry(correct.toFloat()),
                PieEntry(wrong.toFloat())
            ), ""
        ).apply {
            colors = listOf(
                Color.parseColor("#6C4DFF"), // фиолетовый
                Color.parseColor("#E9E7FF")  // серый
            )
            setDrawValues(false)
        }

        chart.data = PieData(dataSet)
        chart.animateY(800)

        val centerText = SpannableString("$percent%\nверных ответов").apply {
            setSpan(RelativeSizeSpan(4f), 0, "$percent%".length, 0)
            setSpan(StyleSpan(Typeface.BOLD), 0, "$percent%".length, 0)
            setSpan(
                ForegroundColorSpan(Color.parseColor("#6C4DFF")),
                0,
                "$percent%".length,
                0
            )
        }

        chart.centerText = centerText
    }



    private fun showEarnedPoints(points: Int) {
        val tv = requireView().findViewById<TextView>(R.id.tvEarnedPoints)
        tv.text = "🔥 Вау! Ты заработал +$points очков"
        tv.visibility = View.VISIBLE
    }

    private fun saveRecentActivity(correct: Int, total: Int) {

        val source = arguments?.getString("source")
        val testTopic = arguments?.getString("testTopic")
        val topicIcon = arguments?.getString("topicIcon")

        // 🧠 Если тест с главной — это быстрый тест
        val isQuickTest = source == "home"

        // 🏷 Название категории
        val category = if (isQuickTest) {
            "Быстрый тест"
        } else {
            testTopic ?: "Общий"
        }

        // 🎨 Иконка
        val icon = if (isQuickTest) {
            "🧠"
        } else {
            topicIcon ?: "📚"
        }

        // 📝 Описание
        val description = if (isQuickTest) {
            "Быстрый тест: правильных ответов $correct из $total"
        } else {
            "Тема «$category»: $correct из $total правильных"
        }

        RecentActivityPrefs.add(
            requireContext(),
            ActivityItem(
                iconEmoji = icon,
                title = "Пройден тест",
                category = category,
                timestamp = System.currentTimeMillis(),
                description = description,
                points = correct * 2,
                correct = correct,
                total = total
            )
        )
    }


    private fun showErrors(errors: List<WrongAnswer>) {
        errorsContainer.removeAllViews()

        errors.forEach { error ->
            val v =
                layoutInflater.inflate(R.layout.item_test_incorrect_word, errorsContainer, false)

            val tvWord = v.findViewById<TextView>(R.id.tvWord)
            val tvTranslation = v.findViewById<TextView>(R.id.tvTranslation)
            val tvTopic = v.findViewById<TextView>(R.id.tvTopic)
            val btnAdd = v.findViewById<TextView>(R.id.tvAddToUnknown)
            val heart = v.findViewById<TextView>(R.id.ivStatusEmoji)

            tvWord.text = error.word
            tvTranslation.text = error.translation
            tvTopic.text = error.topic

            lifecycleScope.launch {
                val entity = repository.getWordById(error.wordId) ?: return@launch

                heart.text = if (entity.isFavorite) "❤️" else "🤍"

                heart.setOnClickListener {
                    lifecycleScope.launch {
                        val newState = !entity.isFavorite
                        repository.setFavorite(entity, newState)

                        // ВАЖНО: обновляем локально
                        entity.isFavorite = newState

                        heart.text = if (newState) "❤️" else "🤍"
                    }
                }

                btnAdd.setOnClickListener {
                    lifecycleScope.launch {
                        repository.setLearned(entity, false)
                        btnAdd.text = "✓"
                        btnAdd.isEnabled = false
                    }
                }
            }

            errorsContainer.addView(v)
        }
    }
    private fun goBack() {
        val source = arguments?.getString("source")

        when (source) {
            "home" -> findNavController().popBackStack(R.id.homeFragment, false)
            else -> findNavController().popBackStack(R.id.testsFragment, false)
        }
    }

}
