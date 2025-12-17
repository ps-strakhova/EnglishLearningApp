package com.example.englishlearningapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.englishlearningapp.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel

    // Chart
    private lateinit var progressChart: PieChart

    // Legend
    private lateinit var textStudied: TextView
    private lateinit var textRemaining: TextView

    // Stats cards
    private lateinit var textNewTodayValue: TextView
    private lateinit var textStreakValue: TextView

    // Recycler
    private lateinit var recentRecycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        bindViews(view)
        setupChart()
        observeProgress()
        updateStats()
        setupRecentActivity()
    }

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
        progressChart.setUsePercentValues(false)
    }

    private fun observeProgress() {
        viewModel.learnedWords.observe(viewLifecycleOwner) { learned ->
            viewModel.totalWords.observe(viewLifecycleOwner) { total ->
                val remaining = total - learned
                val percent = if (total > 0) learned * 100 / total else 0
                updateChartUi(learned, remaining, percent)
            }
        }
    }

    private fun updateChartUi(learned: Int, remaining: Int, percent: Int) {
        val entries = listOf(PieEntry(learned.toFloat()), PieEntry(remaining.toFloat()))
        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(Color.parseColor("#6C4DFF"), Color.parseColor("#E9E7FF"))
            setDrawValues(false)
        }
        progressChart.data = PieData(dataSet)
        progressChart.animateY(900, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)

        val centerText = android.text.SpannableString("$percent%\nслов выучено")
        centerText.setSpan(
            android.text.style.RelativeSizeSpan(2f),
            0,
            "$percent%".length,
            android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        centerText.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            "$percent%".length,
            android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        centerText.setSpan(
            android.text.style.ForegroundColorSpan(Color.parseColor("#6C4DFF")),
            0,
            "$percent%".length,
            android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        progressChart.centerText = centerText

        textStudied.text = "$learned выучено"
        textRemaining.text = "$remaining осталось"
    }

    private fun updateStats() {
        textNewTodayValue.text = viewModel.newWordsToday.toString()
        textStreakValue.text = viewModel.streakDays.toString()
    }


    private fun setupRecentActivity() {
        recentRecycler.layoutManager = LinearLayoutManager(requireContext())
        recentRecycler.adapter = RecentActivityAdapter(viewModel.recentActivities) { item ->
            ActivityDetailsDialogFragment(
                icon = item.iconEmoji,
                titleText = item.title,
                descriptionText = item.description,
                pointsValue = item.points
            ).show(parentFragmentManager, "activity_dialog")
        }
    }
}
