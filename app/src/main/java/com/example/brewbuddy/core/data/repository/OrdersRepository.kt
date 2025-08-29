package com.example.brewbuddy.core.data.repository

import com.example.brewbuddy.core.model.Order
import com.example.brewbuddy.core.model.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun getAllOrders(): Flow<List<Order>>
    suspend fun placeOrder(items: List<OrderItem>): String
    suspend fun deleteOrder(orderId: String)
}