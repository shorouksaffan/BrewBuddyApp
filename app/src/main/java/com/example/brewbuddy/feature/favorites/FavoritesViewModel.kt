package com.example.brewbuddy.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.repository.FavoritesRepository
import com.example.brewbuddy.core.model.Drink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getFavoriteDrinks().collect { favorites ->
                _uiState.value = FavoritesUiState(
                    favorites = favorites,
                    isLoading = false,
                    isEmpty = favorites.isEmpty()
                )
            }
        }
    }

    fun removeFromFavorites(drinkId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorites(drinkId)
            // No need to manually refresh - flow will update automatically
        }
    }

    fun toggleFavorite(drinkId: Int, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFavorite) {
                repository.removeFromFavorites(drinkId)
            } else {
                // You'll need to add addToFavorites method to your repository
                // repository.addToFavorites(drinkId)
            }
        }
    }
}

data class FavoritesUiState(
    val favorites: List<Drink> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null
)