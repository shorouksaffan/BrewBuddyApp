package com.example.brewbuddy.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import java.util.Locale

@Parcelize
data class Money(
    val amount: Double,
    val currency: String = "USD"
) : Parcelable {
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