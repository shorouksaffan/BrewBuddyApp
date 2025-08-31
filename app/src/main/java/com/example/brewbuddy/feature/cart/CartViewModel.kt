package com.example.brewbuddy.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.repository.CartRepository
import com.example.brewbuddy.core.data.repository.OrdersRepository
import com.example.brewbuddy.core.model.CartItem
import com.example.brewbuddy.core.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { cartItems ->
                val total = cartRepository.getCartTotal()
                _uiState.value = CartUiState(
                    cartItems = cartItems,
                    totalAmount = total,
                    isEmpty = cartItems.isEmpty()
                )
            }
        }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(item.drink.id, newQuantity)
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(item.drink.id)
        }
    }

    fun placeOrder() {
        viewModelScope.launch {
            val cartItems = _uiState.value.cartItems
            if (cartItems.isNotEmpty()) {
                val orderItems = cartItems.map { cartItem ->
                    OrderItem(
                        drinkId = cartItem.drink.id,
                        drinkName = cartItem.drink.name,
                        drinkImage = cartItem.drink.imageUrl,
                        price = cartItem.drink.price,
                        quantity = cartItem.quantity
                    )
                }
                ordersRepository.placeOrder(orderItems)
                cartRepository.clearCart()
                _uiState.value = _uiState.value.copy(
                    orderPlaced = true,
                    cartItems = emptyList(),
                    isEmpty = true
                )
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val isEmpty: Boolean = true,
    val orderPlaced: Boolean = false,
    val isLoading: Boolean = false
)