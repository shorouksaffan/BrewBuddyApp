package com.example.brewbuddy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.remote.ApiResult
import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.prefs.UserPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DrinkRepository,
    private val userPrefs: UserPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            repository.getAllDrinks().collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is ApiResult.Success -> {
                        val drinks = result.data
                        if (drinks.isNotEmpty()) {
                            val bestSeller = drinks.maxByOrNull { it.price.amount } ?: drinks.random()
                            val recommendations = drinks.filter { it != bestSeller }.shuffled().take(6)

                            _uiState.value = HomeUiState(
                                userName = userPrefs.userName,
                                bestSeller = bestSeller,
                                recommendations = recommendations,
                                isLoading = false,
                                error = null
                            )
                        } else {
                            _uiState.value = HomeUiState(
                                error = "No drinks available",
                                isLoading = false
                            )
                        }
                    }
                    is ApiResult.Failure -> {
                        _uiState.value = HomeUiState(
                            error = result.exception.message ?: "Failed to load data",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}

data class HomeUiState(
    val userName: String = "",
    val bestSeller: Drink? = null,
    val recommendations: List<Drink> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
