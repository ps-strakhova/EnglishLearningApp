package com.example.englishlearningapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.englishlearningapp.R

class ActivityDetailsDialogFragment(
    private val icon: String,
    private val titleText: String,
    private val descriptionText: String,
    private val pointsValue: Int
) : DialogFragment() {

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
    ): View {
        return inflater.inflate(R.layout.dialog_activity_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.icon).text = icon
        view.findViewById<TextView>(R.id.title).text = titleText
        view.findViewById<TextView>(R.id.description).text = descriptionText
        view.findViewById<TextView>(R.id.points).text = "+$pointsValue баллов"

        view.findViewById<TextView>(R.id.btnClose).setOnClickListener {
            dismiss()
        }

        view.findViewById<TextView>(R.id.btnContinue).setOnClickListener {
            dismiss()
            // позже сюда добавим переход к обучению
        }
    }
}

