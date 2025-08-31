package com.example.brewbuddy.core.model

data class OrderItem(
    val drinkId: Int,
    val drinkName: String,
    val drinkImage: String,
    val price: Money,
    var quantity: Int=1
) {
    fun incrementQuantity() = quantity++
    fun decrementQuantity() = if (quantity > 1) quantity-- else 1

}
