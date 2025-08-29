package com.example.brewbuddy.core.model

data class Order(
    val orderId: String,
    val items: List<OrderItem>,
    val totalAmount: Money,
    val placedAt: Long
) {
    val itemCount: Int
        get() = items.sumOf { it.quantity }
}