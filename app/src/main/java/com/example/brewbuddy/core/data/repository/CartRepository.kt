package com.example.brewbuddy.core.data.repository

import com.example.brewbuddy.core.model.CartItem
import com.example.brewbuddy.core.model.Drink
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(drink: Drink, quantity: Int = 1)
    suspend fun updateQuantity(drinkId: Int, quantity: Int)
    suspend fun removeFromCart(drinkId: Int)
    suspend fun clearCart()
    suspend fun getCartTotal(): Double
    suspend fun getItemCount(): Int
}