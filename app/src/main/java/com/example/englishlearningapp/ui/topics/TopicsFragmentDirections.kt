package com.example.englishlearningapp.ui.topics

import androidx.navigation.NavDirections
import android.os.Bundle
import com.example.englishlearningapp.R

class TopicsFragmentDirections private constructor() {
    companion object {
        fun actionTopicsToWords(topicName: String): NavDirections {
            return object : NavDirections {
                override val actionId: Int
                    get() = R.id.action_topics_to_words
                override val arguments: Bundle
                    get() = Bundle().apply { putString("topicName", topicName) }
            }
        }
    }
}
