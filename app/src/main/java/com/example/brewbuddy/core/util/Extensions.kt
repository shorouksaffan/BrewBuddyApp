package com.example.brewbuddy.core.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Context Extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// Flow Extensions
fun <T> Flow<T>.observeIn(lifecycleOwner: LifecycleOwner, action: suspend (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        collect { action(it) }
    }
}

// String Extensions
fun String.isValidName(): Boolean {
    return trim().length >= 2 && all { it.isLetter() || it.isWhitespace() }
}

fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }
}

// List Extensions
fun <T> List<T>.safe(index: Int): T? {
    return if (index in 0 until size) this[index] else null
}

// Double Extensions
fun Double.toCurrency(): String {
    return String.format("$%.2f", this)
}