package com.example.englishlearningapp.data.model

import kotlin.math.abs

object TimeAgoUtils {

    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = abs(now - timestamp)

        val minute = 60_000L
        val hour = 60 * minute
        val day = 24 * hour

        return when {
            diff < minute -> "только что"
            diff < hour -> {
                val m = diff / minute
                "$m ${minutesWord(m.toInt())} назад"
            }
            diff < day -> {
                val h = diff / hour
                "$h ${hoursWord(h.toInt())} назад"
            }
            diff < 2 * day -> "вчера"
            else -> {
                val d = diff / day
                "$d ${daysWord(d.toInt())} назад"
            }
        }
    }

    private fun minutesWord(n: Int) =
        when {
            n % 100 in 11..19 -> "минут"
            n % 10 == 1 -> "минуту"
            n % 10 in 2..4 -> "минуты"
            else -> "минут"
        }

    private fun hoursWord(n: Int) =
        when {
            n % 100 in 11..19 -> "часов"
            n % 10 == 1 -> "час"
            n % 10 in 2..4 -> "часа"
            else -> "часов"
        }

    private fun daysWord(n: Int) =
        when {
            n % 100 in 11..19 -> "дней"
            n % 10 == 1 -> "день"
            n % 10 in 2..4 -> "дня"
            else -> "дней"
        }
}
