package com.example.brewbuddy.core.data.repository

import com.example.brewbuddy.core.model.Drink
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteDrinks(): Flow<List<Drink>>
    suspend fun isFavorite(drinkId: Int): Boolean
    suspend fun addToFavorites(drinkId: Int)
    suspend fun removeFromFavorites(drinkId: Int)
}