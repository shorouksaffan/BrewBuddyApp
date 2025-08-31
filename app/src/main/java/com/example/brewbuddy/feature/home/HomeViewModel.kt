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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DrinkRepository,
    private val userPrefs: UserPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            repository.getAllDrinks().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val drinks = result.data
                        val bestSeller = getBestSeller(drinks)
                        val recommendations = getRecommendations(drinks, bestSeller)
                        _uiState.value = HomeUiState(
                            userName = userPrefs.userName,
                            bestSeller = bestSeller,
                            recommendations = recommendations,
                            isLoading = false,
                            error = null
                        )
                    }
                    is ApiResult.Failure -> {
                        _uiState.value = HomeUiState(
                            error = result.exception.message ?: "Failed to load data",
                            isLoading = false
                        )
                    }
                    ApiResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun getBestSeller(drinks: List<Drink>): Drink? {
        return drinks.maxByOrNull { it.price.amount } ?: drinks.randomOrNull()
    }

    private fun getRecommendations(drinks: List<Drink>, bestSeller: Drink?): List<Drink> {
        val filtered = bestSeller?.let { bs -> drinks.filter { it.id != bs.id } } ?: drinks
        return filtered.shuffled().take(6)
    }

}

data class HomeUiState(
    val userName: String = "",
    val bestSeller: Drink? = null,
    val recommendations: List<Drink> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
