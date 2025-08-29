package com.example.brewbuddy.core.data.repository.impl

import com.example.brewbuddy.core.data.local.dao.DrinkCacheDao
import com.example.brewbuddy.core.data.local.dao.FavoriteDao
import com.example.brewbuddy.core.data.local.entity.FavoriteEntity
import com.example.brewbuddy.core.data.repository.FavoritesRepository
import com.example.brewbuddy.core.data.repository.toDrink
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.util.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val drinkCacheDao: DrinkCacheDao,
    private val dispatchersProvider: DispatchersProvider
) : FavoritesRepository {

    override fun getFavoriteDrinks(): Flow<List<Drink>> {
        return favoriteDao.getFavoriteIds()
            .map { favoriteIds ->
                favoriteIds.mapNotNull { id ->
                    drinkCacheDao.getDrinkById(id)?.toDrink()
                }
            }
            .flowOn(dispatchersProvider.io)
    }

    override suspend fun isFavorite(drinkId: Int): Boolean {
        return withContext(dispatchersProvider.io) {
            favoriteDao.isFavorite(drinkId)
        }
    }

    override suspend fun addToFavorites(drinkId: Int) {
        withContext(dispatchersProvider.io) {
            favoriteDao.addFavorite(FavoriteEntity(drinkId))
        }
    }

    override suspend fun removeFromFavorites(drinkId: Int) {
        withContext(dispatchersProvider.io) {
            favoriteDao.removeFavorite(drinkId)
        }
    }
}