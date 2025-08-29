package com.example.brewbuddy.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatters {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val fullDateFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())

    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatFullDate(timestamp: Long): String {
        return fullDateFormat.format(Date(timestamp))
    }

    fun formatOrderDate(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 24 * 60 * 60 * 1000 -> "Today at ${formatTime(timestamp)}"
            diff < 2 * 24 * 60 * 60 * 1000 -> "Yesterday at ${formatTime(timestamp)}"
            else -> formatFullDate(timestamp)
        }
    }
}