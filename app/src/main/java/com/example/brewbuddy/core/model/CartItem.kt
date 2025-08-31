package com.example.brewbuddy.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val drink: Drink,
    var quantity: Int = 1,
    val addedAt: Long = System.currentTimeMillis()
) : Parcelable {
    val totalPrice: Money
        get() = drink.price * quantity
}