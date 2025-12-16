package com.example.englishlearningapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class HomeFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        bindViews(view)
        setupChart()
        updateChart()
        updateStats()
        setupRecentActivity()
        setupQuizButton(view)
    }

    // -------------------- Views --------------------

    private fun bindViews(view: View) {
        progressChart = view.findViewById(R.id.progressChart)
        textStudied = view.findViewById(R.id.textStudied)
        textRemaining = view.findViewById(R.id.textRemaining)
        textNewTodayValue = view.findViewById(R.id.textNewTodayValue)
        textStreakValue = view.findViewById(R.id.textStreakValue)
        recentRecycler = view.findViewById(R.id.recentRecycler)
    }

    // -------------------- Chart --------------------

    private fun setupChart() {
        progressChart.description.isEnabled = false
        progressChart.legend.isEnabled = false
        progressChart.setDrawEntryLabels(false)
        progressChart.isDrawHoleEnabled = true
        progressChart.holeRadius = 72f
        progressChart.transparentCircleRadius = 76f
        progressChart.setUsePercentValues(false)
    }

    private fun updateChart() {
        val learned = viewModel.learnedCount
        val total = viewModel.totalCount
        val remaining = total - learned
        val percent = if (total > 0) learned * 100 / total else 0

        val entries = listOf(
            PieEntry(learned.toFloat()),
            PieEntry(remaining.toFloat())
        )

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(
                Color.parseColor("#6C4DFF"), // primary
                Color.parseColor("#E9E7FF")  // remaining
            )
            setDrawValues(false)
        }

        progressChart.data = PieData(dataSet)
        progressChart.animateY(
            900,
            com.github.mikephil.charting.animation.Easing.EaseInOutQuad
        )


        // Центр диаграммы
        val centerText = android.text.SpannableString("$percent%\nслов выучено")

// Процент — крупный и фиолетовый
        centerText.setSpan(
            android.text.style.RelativeSizeSpan(4f),
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
            android.text.style.ForegroundColorSpan(
                Color.parseColor("#6C4DFF")
            ),
            0,
            "$percent%".length,
            android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

// Подпись — мелкая и серая
        val subtitleStart = "$percent%\n".length
        centerText.setSpan(
            android.text.style.RelativeSizeSpan(1f),
            subtitleStart,
            centerText.length,
            android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        centerText.setSpan(
            android.text.style.ForegroundColorSpan(
                Color.parseColor("#94A3B8") // text_muted
            ),
            subtitleStart,
            centerText.length,
            android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        progressChart.centerText = centerText


        // Legend text
        textStudied.text = "$learned выучено"
        textRemaining.text = "$remaining осталось"
    }

    // -------------------- Stats --------------------

    private fun updateStats() {
        textNewTodayValue.text = viewModel.newWordsToday.toString()
        textStreakValue.text = viewModel.streakDays.toString()
    }

    // -------------------- Recent Activity --------------------

    private fun setupRecentActivity() {
        recentRecycler.layoutManager = LinearLayoutManager(requireContext())
        recentRecycler.adapter =
            RecentActivityAdapter(viewModel.recentActivities) { item ->
                ActivityDetailsDialogFragment(
                    icon = item.iconEmoji,
                    titleText = item.title,
                    descriptionText = item.description,
                    pointsValue = item.points
                ).show(parentFragmentManager, "activity_dialog")
            }
    }

    // -------------------- Quiz button --------------------

    private fun setupQuizButton(view: View) {
        view.findViewById<View>(R.id.btnQuickQuiz).setOnClickListener {
            // пока заглушка
            // позже сюда добавим переход к экрану тестов
        }
    }
}
