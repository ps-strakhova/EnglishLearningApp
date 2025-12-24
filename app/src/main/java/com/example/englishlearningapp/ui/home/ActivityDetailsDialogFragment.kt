package com.example.englishlearningapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.englishlearningapp.R
import com.example.englishlearningapp.data.model.PluralUtils
import com.example.englishlearningapp.data.model.ActivityItem


class ActivityDetailsDialogFragment : DialogFragment() {

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.dialog_activity_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val icon = requireArguments().getString("icon") ?: "📚"
        val title = requireArguments().getString("title") ?: ""
        val description = requireArguments().getString("description") ?: ""
        val points = requireArguments().getInt("points")

        view.findViewById<TextView>(R.id.icon).text = icon
        view.findViewById<TextView>(R.id.title).text = title
        view.findViewById<TextView>(R.id.description).text = description
        view.findViewById<TextView>(R.id.points).text =
            "+$points ${PluralUtils.pointsWord(points)}"

        view.findViewById<TextView>(R.id.btnClose).setOnClickListener { dismiss() }
        view.findViewById<TextView>(R.id.btnContinue).setOnClickListener { dismiss() }
    }

    companion object {
        fun newInstance(item: ActivityItem): ActivityDetailsDialogFragment {
            return ActivityDetailsDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("icon", item.iconEmoji)
                    putString("title", item.title)
                    putString("description", item.description)
                    putInt("points", item.points)
                }
            }
        }
    }
}
