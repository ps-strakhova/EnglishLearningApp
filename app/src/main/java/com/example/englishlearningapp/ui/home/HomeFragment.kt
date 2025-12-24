package com.example.englishlearningapp.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.database.AppDatabase
import com.example.englishlearningapp.data.model.RecentActivityPrefs
import com.example.englishlearningapp.data.repository.WordRepository
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    // DB
    private lateinit var repository: WordRepository

    // UI
    private lateinit var progressChart: PieChart
    private lateinit var textStudied: TextView
    private lateinit var textRemaining: TextView
    private lateinit var textNewTodayValue: TextView
    private lateinit var textStreakValue: TextView
    private lateinit var recentRecycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB
        val dao = AppDatabase.getDatabase(requireContext()).wordDao()
        repository = WordRepository(dao)

        bindViews(view)
        setupChart()
        setupQuickTest(view)
        setupRecentActivity()

        // первичное обновление
        updateStatsFromDb()
    }

    override fun onResume() {
        super.onResume()
        // 🔥 ОБЯЗАТЕЛЬНО: обновляем при возврате с тестов / профиля
        updateStatsFromDb()
        setupRecentActivity()
    }

    // ---------------- UI ----------------

    private fun bindViews(view: View) {
        progressChart = view.findViewById(R.id.progressChart)
        textStudied = view.findViewById(R.id.textStudied)
        textRemaining = view.findViewById(R.id.textRemaining)
        textNewTodayValue = view.findViewById(R.id.textNewTodayValue)
        textStreakValue = view.findViewById(R.id.textStreakValue)
        recentRecycler = view.findViewById(R.id.recentRecycler)
    }

    private fun setupChart() {
        progressChart.description.isEnabled = false
        progressChart.legend.isEnabled = false
        progressChart.setDrawEntryLabels(false)
        progressChart.isDrawHoleEnabled = true
        progressChart.holeRadius = 72f
        progressChart.transparentCircleRadius = 76f
    }

    // ---------------- DATA ----------------

    private fun updateStatsFromDb() {
        lifecycleScope.launch {
            val learned = repository.getLearnedWordsCount()
            val total = repository.getTotalWordsCount()
            val remaining = total - learned
            val percent = if (total == 0) 0 else learned * 100 / total

            // 🔥 ОСНОВНОЕ
            textStudied.text = "$learned выучено"
            textRemaining.text = "$remaining осталось"

            // 🔥 ТВОЁ ТРЕБОВАНИЕ:
            // число «Новых слов за сегодня» = выучено
            textNewTodayValue.text = learned.toString()

            // пока статично
            textStreakValue.text = "5"

            updateChart(learned, remaining, percent)
        }
    }

    private fun updateChart(learned: Int, remaining: Int, percent: Int) {
        val entries = listOf(
            PieEntry(learned.toFloat()),
            PieEntry(remaining.toFloat())
        )

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(
                Color.parseColor("#6C4DFF"),
                Color.parseColor("#E9E7FF")
            )
            setDrawValues(false)
        }

        progressChart.data = PieData(dataSet)
        progressChart.animateY(600)

        val centerText = SpannableString("$percent%\nслов выучено").apply {
            setSpan(RelativeSizeSpan(4f), 0, "$percent%".length, 0)
            setSpan(StyleSpan(Typeface.BOLD), 0, "$percent%".length, 0)
            setSpan(
                ForegroundColorSpan(Color.parseColor("#6C4DFF")),
                0,
                "$percent%".length,
                0
            )
        }


        progressChart.centerText = centerText
    }

    // ---------------- QUICK TEST ----------------

    private fun setupQuickTest(view: View) {
        val btnQuickQuiz =
            view.findViewById<com.google.android.material.button.MaterialButton>(
                R.id.btnQuickQuiz
            )

        btnQuickQuiz.setOnClickListener {
            val bundle = Bundle().apply {
                putString("testId", "quick_random")   // 👈 ОБЩИЙ ТЕСТ
                putString("testTopic", null)
                putString("source", "home")       // 👈 КРИТИЧЕСКИ ВАЖНО
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_testRunFragment,
                bundle
            )
        }
    }

    // ---------------- RECENT ACTIVITY ----------------

    private fun setupRecentActivity() {
        recentRecycler.layoutManager = LinearLayoutManager(requireContext())

        val activities = RecentActivityPrefs
            .getAll(requireContext())
            .sortedByDescending { it.timestamp }

        recentRecycler.adapter = RecentActivityAdapter(activities) { item ->
            ActivityDetailsDialogFragment
                .newInstance(item)
                .show(parentFragmentManager, "activity_details")
        }
    }
}
