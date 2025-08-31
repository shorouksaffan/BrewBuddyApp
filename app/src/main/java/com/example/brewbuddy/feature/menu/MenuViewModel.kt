package com.example.brewbuddy.feature.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.remote.ApiResult
import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: DrinkRepository,
    private val dispatcher: DispatchersProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MenuEvent>()
    val events: SharedFlow<MenuEvent> = _events.asSharedFlow()

    // All drinks fetched from repo
    private var allDrinks: List<Drink> = emptyList()

    // Current category selection
    enum class Category { ALL, HOT, COLD }

    private var currentCategory: Category = Category.ALL

    fun loadMenu() {
        viewModelScope.launch(dispatcher.io) {
            _uiState.emit(MenuUiState.Loading)
            try {
                val hot = repository.getHotDrinks().first()
                val cold = repository.getColdDrinks().first()

                if (hot is ApiResult.Success && cold is ApiResult.Success) {
                    allDrinks = hot.data + cold.data
                    if (allDrinks.isEmpty()) {
                        _uiState.emit(MenuUiState.Empty)
                    } else {
                        _uiState.emit(MenuUiState.Success(allDrinks))
                    }
                } else {
                    _uiState.emit(MenuUiState.Error("Failed to load drinks"))
                }
            } catch (e: Exception) {
                _uiState.emit(MenuUiState.Error(e.message ?: "Unknown error"))
                _events.emit(MenuEvent.ShowSnackBar("Error loading menu"))
            }
        }
    }

    fun filterByCategory(category: Category) {
        currentCategory = category
        val filtered = when (category) {
            Category.HOT -> allDrinks.filter { it.isHot }
            Category.COLD -> allDrinks.filter { !it.isHot }
            Category.ALL -> allDrinks
        }
        emitFiltered(filtered)
    }

    fun onQueryChanged(query: String) {
        val baseList = when (currentCategory) {
            Category.HOT -> allDrinks.filter { it.isHot }
            Category.COLD -> allDrinks.filter { !it.isHot }
            Category.ALL -> allDrinks
        }
        val filtered = baseList.filter { it.name.contains(query, ignoreCase = true) }
        emitFiltered(filtered)
    }

    private fun emitFiltered(list: List<Drink>) {
        viewModelScope.launch {
            if (list.isEmpty()) {
                _uiState.emit(MenuUiState.Empty)
            } else {
                _uiState.emit(MenuUiState.Success(list))
            }
        }
    }
}

// --- One-time Events ---
sealed class MenuEvent {
    data class ShowSnackBar(val message: String) : MenuEvent()
}
