package com.example.brewbuddy.feature.menu.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DrinkDetailsViewModel @Inject constructor(
    private val drinkRepository: DrinkRepository,
    private val favoritesRepository: FavoritesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<DrinkDetailsUiState>(DrinkDetailsUiState.Loading)
    val uiState: StateFlow<DrinkDetailsUiState> = _uiState.asStateFlow()

    private val drinkId: Int = checkNotNull(savedStateHandle["drinkId"])

    fun loadDrinkDetails() {
        viewModelScope.launch {
            _uiState.emit(DrinkDetailsUiState.Loading)
            try {
                val drink = drinkRepository.getDrinkById(drinkId)
                if (drink != null) {
                    val isFavorite = favoritesRepository.isFavorite(drinkId)
                    _uiState.emit(DrinkDetailsUiState.Success(drink, isFavorite))
                } else {
                    _uiState.emit(DrinkDetailsUiState.Error("Drink not found"))
                }
            } catch (e: Exception) {
                _uiState.emit(DrinkDetailsUiState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun toogleFavorite() {
        viewModelScope.launch {
            val currentUiState = _uiState.value
            if (currentUiState is DrinkDetailsUiState.Success) {
                if (currentUiState.isFavorite)
                    favoritesRepository.removeFromFavorites(drinkId)
                else
                    favoritesRepository.addToFavorites(drinkId)
                _uiState.emit(currentUiState.copy(isFavorite = !currentUiState.isFavorite))
            }
        }
    }
}