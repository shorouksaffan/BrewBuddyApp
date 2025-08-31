package com.example.brewbuddy.core.data.repository.impl

import com.example.brewbuddy.core.data.repository.CartRepository
import com.example.brewbuddy.core.model.CartItem
import com.example.brewbuddy.core.model.Drink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor() : CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override fun getCartItems(): Flow<List<CartItem>> = _cartItems

    override suspend fun addToCart(drink: Drink, quantity: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.drink.id == drink.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentItems.add(CartItem(drink, quantity))
        }

        _cartItems.value = currentItems
    }

    override suspend fun updateQuantity(drinkId: Int, quantity: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val item = currentItems.find { it.drink.id == drinkId }

        if (item != null) {
            if (quantity > 0) {
                item.quantity = quantity
            } else {
                currentItems.remove(item)
            }
            _cartItems.value = currentItems
        }
    }

    override suspend fun removeFromCart(drinkId: Int) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.drink.id == drinkId }
        _cartItems.value = currentItems
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }

    override suspend fun getCartTotal(): Double {
        return _cartItems.value.sumOf { it.totalPrice.amount }
    }

    override suspend fun getItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}