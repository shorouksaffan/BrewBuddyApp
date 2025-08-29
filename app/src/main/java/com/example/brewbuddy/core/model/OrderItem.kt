package com.example.brewbuddy.core.model

data class OrderItem(
    val drinkId: Int,
    val drinkName: String,
    val drinkImage: String,
    val price: Money,
    val quantity: Int
) {
    val totalPrice: Money
        get() = price * quantity
}