package com.example.brewbuddy.feature.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.repository.OrdersRepository
import com.example.brewbuddy.core.model.Order
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

    private val _allOrders = MutableStateFlow<List<Order>>(emptyList())
    private val _filteredOrders = MutableStateFlow<List<Order>>(emptyList())

    // Convert StateFlow to LiveData for observing in Fragment
    val orders: LiveData<List<Order>> = _filteredOrders.asLiveData()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                ordersRepository.getAllOrders().collect { ordersList ->
                    _allOrders.value = ordersList
                    _filteredOrders.value = ordersList
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterOrders(recentOnly: Boolean) {
        viewModelScope.launch {
            if (recentOnly) {
                val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
                _filteredOrders.value = _allOrders.value.filter { order ->
                    order.placedAt >= thirtyDaysAgo
                }
            } else {
                _filteredOrders.value = _allOrders.value
            }
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            ordersRepository.deleteOrder(orderId)
            loadOrders()
        }
    }
}