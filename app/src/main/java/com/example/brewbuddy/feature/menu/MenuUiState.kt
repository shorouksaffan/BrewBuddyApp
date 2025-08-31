package com.example.brewbuddy.feature.menu

import com.example.brewbuddy.core.model.Drink

sealed class MenuUiState {
    object Loading : MenuUiState()
    data class Success(val drinks: List<Drink>) : MenuUiState()
    data class Error(val message: String) : MenuUiState()
    object Empty : MenuUiState()
}