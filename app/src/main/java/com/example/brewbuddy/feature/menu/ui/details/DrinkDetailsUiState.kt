package com.example.brewbuddy.feature.menu.ui.details

import com.example.brewbuddy.core.model.Drink

sealed class DrinkDetailsUiState {
    object Loading : DrinkDetailsUiState()
    data class Success(val drink: Drink, val isFavorite: Boolean) : DrinkDetailsUiState()
    data class Error(val message: String) : DrinkDetailsUiState()
}