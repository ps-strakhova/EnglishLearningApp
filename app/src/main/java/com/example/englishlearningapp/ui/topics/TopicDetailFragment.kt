package com.example.englishlearningapp.ui.topics

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.example.englishlearningapp.R


// Это экран, который появляется при нажатии на тему (то есть это список карточек темы)

class TopicDetailFragment : Fragment() {

    private var topicId: Int = -1
    private var topicTitle: String? = null

    private lateinit var recycler: RecyclerView
    private lateinit var titleView: TextView
    private lateinit var countView: TextView
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topicId = it.getInt("topicId", -1)
            topicTitle = it.getString("topicTitle")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_topic_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titleView = view.findViewById(R.id.textTopicTitle)
        countView = view.findViewById(R.id.textCardsCount)
        backBtn = view.findViewById(R.id.btnBack)
        recycler = view.findViewById(R.id.cardsRecycler)

        titleView.text = topicTitle ?: "Тема"

        val demoCards = generateDemoCards(topicId)
        countView.text = "${demoCards.size} cards"

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = CardsAdapter(demoCards)
        recycler.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // ---------------- DEMO DATA ----------------

    private fun generateDemoCards(topicId: Int): List<Card> {
        val words = listOf(
            "Hello", "Goodbye", "Thank you", "Please",
            "Excuse me", "Good morning", "Good night", "See you"
        )

        val translations = listOf(
            "Привет", "Пока", "Спасибо", "Пожалуйста",
            "Извините", "Доброе утро", "Спокойной ночи", "Увидимся"
        )

        val levels = listOf("A1","A1","A1","A1","A2","A1","A1","A1")
        val statuses = listOf("Learned","Review","Learned","New","Review","New","New","Review")

        return words.mapIndexed { index, word ->
            Card(
                id = topicId * 1000 + index,
                title = word,
                translation = translations[index],
                icon = when (index % 6) {
                    0 -> "👋"
                    1 -> "🖐️"
                    2 -> "🙏"
                    3 -> "🙂"
                    4 -> "🚶"
                    else -> "📘"
                },
                status = statuses[index],
                level = levels[index]
            )
        }
    }

    // ---------------- MODEL ----------------

    data class Card(
        val id: Int,
        val title: String,
        val translation: String,
        val icon: String,
        val status: String,
        val level: String,
        var isFlipped: Boolean = false
    )

    // ---------------- ADAPTER ----------------

    class CardsAdapter(
        private val items: List<Card>
    ) : RecyclerView.Adapter<CardsAdapter.VH>() {

        inner class VH(item: View) : RecyclerView.ViewHolder(item) {
            val icon: TextView = item.findViewById(R.id.item_icon)
            val title: TextView = item.findViewById(R.id.item_title)
            val badge: TextView = item.findViewById(R.id.item_badge)
            val level: TextView = item.findViewById(R.id.item_level)
            val action: ImageView = item.findViewById(R.id.item_action)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_topic_card, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val card = items[position]

            // --- ICON & LEVEL ---
            holder.icon.text = card.icon
            holder.level.text = "• ${card.level}"

            // --- TITLE (EN / RU) ---
            holder.title.text = if (card.isFlipped) {
                card.translation
            } else {
                card.title
            }

            // --- BADGE TEXT ---
            holder.badge.text = card.status

            // --- BADGE COLORS ---
            val bgColor = when (card.status) {
                "Learned" -> 0xFFDDF6E9.toInt()   // light green
                "Review"  -> 0xFFFFF0E6.toInt()   // light orange
                "New"     -> 0xFFEAF4FF.toInt()   // light blue
                else      -> 0xFFEAEAEA.toInt()
            }

            val textColor = when (card.status) {
                "Learned" -> 0xFF2E7D32.toInt()
                "Review"  -> 0xFFB35A00.toInt()
                "New"     -> 0xFF1565C0.toInt()
                else      -> 0xFF333333.toInt()
            }

            val radius = holder.badge.context.resources.displayMetrics.density * 12

            holder.badge.background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = radius
                setColor(bgColor)
            }
            holder.badge.setTextColor(textColor)

            // --- FLIP ACTION (ROTATE BUTTON) ---
            holder.action.setOnClickListener {
                card.isFlipped = !card.isFlipped

                holder.title.text = if (card.isFlipped) {
                    card.translation
                } else {
                    card.title
                }
            }

        }


        override fun getItemCount(): Int = items.size
    }
}
