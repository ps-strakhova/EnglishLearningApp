package com.example.englishlearningapp.data.model

object PluralUtils {

    fun pointsWord(count: Int): String {
        val rem100 = count % 100
        val rem10 = count % 10

        return when {
            rem100 in 11..19 -> "очков"
            rem10 == 1 -> "очко"
            rem10 in 2..4 -> "очка"
            else -> "очков"
        }
    }
}