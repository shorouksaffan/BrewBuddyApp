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
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Drink>>(emptyList())
    val favorites: StateFlow<List<Drink>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesRepository.getFavoriteDrinks().collect { favorites ->
                _favorites.value = favorites
            }
        }
    }

    fun removeFromFavorites(drinkId: Int) {
        viewModelScope.launch {
            favoritesRepository.removeFromFavorites(drinkId)
            // The flow will automatically update and trigger a new collection
        }
    }
}