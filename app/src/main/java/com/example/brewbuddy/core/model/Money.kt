package com.example.brewbuddy.core.model

import java.text.NumberFormat
import java.util.Locale

data class Money(
    val amount: Double,
    val currency: String = "USD"
) {
    fun formatted(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        return formatter.format(amount)
    }

    operator fun plus(other: Money): Money {
        require(currency == other.currency) { "Currency mismatch" }
        return Money(amount + other.amount, currency)
    }

    operator fun times(multiplier: Int): Money {
        return Money(amount * multiplier, currency)
    }
}