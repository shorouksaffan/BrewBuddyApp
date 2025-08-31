package com.example.brewbuddy.feature.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.remote.ApiResult
import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.model.Drink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: DrinkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MenuEvent>()
    val events: SharedFlow<MenuEvent> = _events.asSharedFlow()

    private var allDrinks: List<Drink> = emptyList()
    private var currentCategory: Category = Category.ALL
    private var currentQuery: String = ""

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch {
            _uiState.value = MenuUiState.Loading

            repository.getAllDrinks().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        allDrinks = result.data
                        applyFilters()
                    }
                    is ApiResult.Failure -> {
                        _uiState.value =
                            MenuUiState.Error("Failed to load menu: ${result.exception.message}")
                        _events.emit(MenuEvent.ShowSnackBar("Error loading menu"))
                    }
                    ApiResult.Loading -> {
                        _uiState.value = MenuUiState.Loading
                    }
                }
            }
        }
    }

    fun filterByCategory(category: Category) {
        currentCategory = category
        applyFilters()
    }

    fun search(query: String) {
        currentQuery = query
        applyFilters()
    }

    private fun applyFilters() {
        var filtered = allDrinks

        // Category filter
        filtered = when (currentCategory) {
            Category.HOT -> filtered.filter { it.isHot }
            Category.COLD -> filtered.filter { !it.isHot }
            Category.ALL -> filtered
        }

        // Search filter
        if (currentQuery.isNotBlank()) {
            filtered = filtered.filter { drink ->
                drink.name.contains(currentQuery, ignoreCase = true) ||
                        drink.description.contains(currentQuery, ignoreCase = true) ||
                        drink.ingredients.any { it.contains(currentQuery, ignoreCase = true) }
            }
        }

        // Update UI state
        _uiState.value = if (filtered.isEmpty()) {
            MenuUiState.Empty
        } else {
            MenuUiState.Success(filtered)
        }
    }

    // Convenience for SearchView listeners
    fun onQueryChanged(query: String) {
        search(query)
    }
}

// ---- Supporting types ----

sealed class MenuEvent {
    data class ShowSnackBar(val message: String) : MenuEvent()
    data class NavigateToDetails(val drinkId: Int) : MenuEvent()
}

enum class Category { ALL, HOT, COLD }