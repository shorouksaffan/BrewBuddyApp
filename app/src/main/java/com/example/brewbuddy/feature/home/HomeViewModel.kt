package com.example.brewbuddy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.core.data.remote.ApiResult
import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.model.Drink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(  // ADD @Inject constructor
    private val repository: DrinkRepository
) : ViewModel() {

    private val _bestSeller = MutableStateFlow<Drink?>(null)
    val bestSeller: StateFlow<Drink?> = _bestSeller.asStateFlow()

    private val _recommendations = MutableStateFlow<List<Drink>>(emptyList())
    val recommendations: StateFlow<List<Drink>> = _recommendations.asStateFlow()

    private val _allDrinks = MutableStateFlow<ApiResult<List<Drink>>>(ApiResult.Loading)
    val allDrinks: StateFlow<ApiResult<List<Drink>>> = _allDrinks.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllDrinks().collect { result ->
                _allDrinks.value = result

                when (result) {
                    is ApiResult.Success -> {
                        val drinks = result.data
                        _bestSeller.value = getBestSeller(drinks)
                        _recommendations.value = getRecommendations(drinks, _bestSeller.value)
                    }
                    else -> {
                        _bestSeller.value = null
                        _recommendations.value = emptyList()
                    }
                }
                _isLoading.value = false
            }
        }
    }

    private fun getBestSeller(drinks: List<Drink>): Drink? {
        return drinks.maxByOrNull { it.price.amount } ?: drinks.randomOrNull()
    }

    private fun getRecommendations(drinks: List<Drink>, bestSeller: Drink?): List<Drink> {
        val filtered = if (bestSeller != null) {
            drinks.filter { it.id != bestSeller.id }
        } else {
            drinks
        }
        return filtered.shuffled().take(6)
    }
}