package com.example.brewbuddy.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.repository.OrdersRepository
import com.example.brewbuddy.core.model.Order
import com.example.brewbuddy.core.util.DateFormatters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    private var allOrders: List<Order> = emptyList()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            ordersRepository.getAllOrders().collect { orders ->
                allOrders = orders
                _uiState.value = OrdersUiState(
                    orders = orders,
                    filteredOrders = orders,
                    isLoading = false,
                    isEmpty = orders.isEmpty()
                )
            }
        }
    }

    fun filterOrders(recentOnly: Boolean) {
        viewModelScope.launch {
            val filtered = if (recentOnly) {
                val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
                allOrders.filter { it.placedAt >= thirtyDaysAgo }
            } else {
                allOrders
            }

            _uiState.value = _uiState.value.copy(
                filteredOrders = filtered,
                showingRecentOnly = recentOnly
            )
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            ordersRepository.deleteOrder(orderId)
            loadOrders() // Reload to refresh the list
        }
    }

    fun formatOrderDate(timestamp: Long): String {
        return DateFormatters.formatOrderDate(timestamp)
    }

}

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val filteredOrders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val showingRecentOnly: Boolean = false,
    val error: String? = null
)