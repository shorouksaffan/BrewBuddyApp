package com.example.brewbuddy.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.repository.FavoritesRepository
import com.example.brewbuddy.core.model.Drink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Drink>>(emptyList())
    val favorites: StateFlow<List<Drink>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteDrinks().collect {
                _favorites.value = it
            }
        }
    }

    fun removeFromFavorites(drinkId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorites(drinkId)
            // refresh
            loadFavorites()
        }
    }
}
