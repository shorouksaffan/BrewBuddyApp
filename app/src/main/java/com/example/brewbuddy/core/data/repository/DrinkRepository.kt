package com.example.brewbuddy.core.data.repository

import com.example.brewbuddy.core.data.remote.ApiResult
import com.example.brewbuddy.core.model.Drink
import kotlinx.coroutines.flow.Flow

interface DrinkRepository {
    suspend fun getBestSeller(): Drink
    suspend fun getRecommendations(count: Int = 6): List<Drink>
    fun getHotDrinks(): Flow<ApiResult<List<Drink>>>
    fun getColdDrinks(): Flow<ApiResult<List<Drink>>>
    fun getAllDrinks(): Flow<ApiResult<List<Drink>>>
    fun searchDrinks(query: String): Flow<List<Drink>>
    suspend fun getDrinkById(drinkId: Int): Drink?
    suspend fun refreshDrinks(): ApiResult<Unit>
}