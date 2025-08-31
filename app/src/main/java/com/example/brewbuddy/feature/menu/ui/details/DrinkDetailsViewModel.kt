package com.example.brewbuddy.feature.menu.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.data.repository.FavoritesRepository
import com.example.brewbuddy.core.data.repository.OrdersRepository
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkDetailsViewModel @Inject constructor(
    private val drinkRepository: DrinkRepository,
    private val favoritesRepository: FavoritesRepository,
    private val ordersRepository: OrdersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<DrinkDetailsUiState>(DrinkDetailsUiState.Loading)
    val uiState: StateFlow<DrinkDetailsUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val drinkId: Int = checkNotNull(savedStateHandle["drinkId"])

    init {
        loadDrinkDetails()
    }

    fun loadDrinkDetails() {
        viewModelScope.launch {
            _uiState.value = DrinkDetailsUiState.Loading
            try {
                val drink = drinkRepository.getDrinkById(drinkId)
                if (drink != null) {
                    val favoriteStatus = favoritesRepository.isFavorite(drinkId)
                    _isFavorite.value = favoriteStatus
                    _uiState.value = DrinkDetailsUiState.Success(drink)
                } else {
                    _uiState.value = DrinkDetailsUiState.Error("Drink not found")
                }
            } catch (e: Exception) {
                _uiState.value = DrinkDetailsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentlyFavorite = _isFavorite.value
            if (currentlyFavorite) {
                favoritesRepository.removeFromFavorites(drinkId)
            } else {
                favoritesRepository.addToFavorites(drinkId)
            }
            _isFavorite.value = !currentlyFavorite

            // No need to update UI state since we have separate favorite state flow
            // The favorite icon will update through observeFavoriteState()
        }
    }

    fun addToCart(quantity: Int) {
        viewModelScope.launch {
            val currentUiState = _uiState.value
            if (currentUiState is DrinkDetailsUiState.Success) {
                val drink = currentUiState.drink
                val orderItem = OrderItem(
                    drinkId = drink.id,
                    drinkName = drink.name,
                    drinkImage = drink.imageUrl,
                    price = drink.price,
                    quantity = quantity
                )
                ordersRepository.placeOrder(listOf(orderItem))
            }
        }
    }
}

sealed class DrinkDetailsUiState {
    object Loading : DrinkDetailsUiState()
    data class Success(val drink: Drink) : DrinkDetailsUiState()
    data class Error(val message: String) : DrinkDetailsUiState()
}