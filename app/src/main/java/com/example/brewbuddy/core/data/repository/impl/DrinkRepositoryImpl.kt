package com.example.brewbuddy.core.data.repository.impl

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.brewbuddy.core.data.local.dao.DrinkCacheDao
import com.example.brewbuddy.core.data.remote.ApiManager
import com.example.brewbuddy.core.data.remote.ApiResult
import com.example.brewbuddy.core.data.remote.ApiResult.*
import com.example.brewbuddy.core.data.remote.CoffeeApiService
import com.example.brewbuddy.core.data.remote.dto.toDrinkCacheEntity
import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.data.repository.toDrink
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.util.DispatchersProvider
import com.example.brewbuddy.core.util.PriceCatalog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DrinkRepositoryImpl @Inject constructor(
    private val apiService: CoffeeApiService,
    private val drinkCacheDao: DrinkCacheDao,
    private val dispatchersProvider: DispatchersProvider
) : DrinkRepository {
    override suspend fun getBestSeller(): Drink {
        return withContext(dispatchersProvider.io) {
            val allDrinks = drinkCacheDao.getAllDrinks().first()
            val bestSellerName = PriceCatalog.getBestSeller()
            allDrinks.find { it.title.equals(bestSellerName, ignoreCase = true) }
                ?.toDrink() ?: allDrinks.random().toDrink()
        }
    }

    override suspend fun getRecommendations(count: Int): List<Drink> {
        return withContext(dispatchersProvider.io) {
            val allDrinks = drinkCacheDao.getAllDrinks().first()
            val recommendedNames = PriceCatalog.getRecommendedDrinks(count)

            recommendedNames.mapNotNull { name ->
                allDrinks.find { it.title.equals(name, ignoreCase = true) }?.toDrink()
            }.take(count)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun getHotDrinks(): Flow<ApiResult<List<Drink>>> = flow {
        val cachedDrinks = drinkCacheDao.getDrinksByType(isHot = true)
            .map { entities -> entities.map { it.toDrink() } }
            .first()

        if (cachedDrinks.isNotEmpty()) {
            emit(ApiResult.Success(cachedDrinks))
        } else {
            val apiResult = ApiManager.execute {
                apiService.getHotCoffees()
            }

            when (apiResult) {
                is Success -> {
                    val entities = apiResult.data.map { it.toDrinkCacheEntity(isHot = true) }
                    drinkCacheDao.insertDrinks(entities)
                    emit(Success(entities.map { it.toDrink() }))
                }

                is Failure -> {
                    emit(Failure(apiResult.exception))
                }

                Loading -> TODO()
            }
        }
    }.flowOn(dispatchersProvider.io)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun getColdDrinks(): Flow<ApiResult<List<Drink>>> = flow {
        val cachedDrinks = drinkCacheDao.getDrinksByType(isHot = false)
            .map { entities -> entities.map { it.toDrink() } }
            .first()

        if (cachedDrinks.isNotEmpty()) {
            emit(ApiResult.Success(cachedDrinks))
        } else {
            val apiResult = ApiManager.execute {
                apiService.getIcedCoffees()
            }

            when (apiResult) {
                is Success -> {
                    val entities = apiResult.data.map { it.toDrinkCacheEntity(isHot = false) }
                    drinkCacheDao.insertDrinks(entities)
                    emit(Success(entities.map { it.toDrink() }))
                }

                is Failure -> {
                    emit(Failure(apiResult.exception))
                }

                Loading -> TODO()
            }
        }
    }.flowOn(dispatchersProvider.io)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun getAllDrinks(): Flow<ApiResult<List<Drink>>> = flow {
        val cachedDrinks = drinkCacheDao.getAllDrinks().first()

        if (cachedDrinks.isNotEmpty()) {
            emit(ApiResult.Success(cachedDrinks.map { it.toDrink() }))
        } else {
            val refreshResult = refreshDrinksInternal()
            when (refreshResult) {
                is ApiResult.Success -> {
                    val freshDrinks = drinkCacheDao.getAllDrinks().first()
                    emit(Success(freshDrinks.map { it.toDrink() }))
                }

                is ApiResult.Failure -> {
                    emit(refreshResult)
                }

                ApiResult.Loading -> TODO()
            }
        }
    }.flowOn(dispatchersProvider.io)

    override fun searchDrinks(query: String): Flow<List<Drink>> {
        return drinkCacheDao.searchDrinks(query)
            .map { entities -> entities.map { it.toDrink() } }
            .flowOn(dispatchersProvider.io)
    }

    override suspend fun getDrinkById(drinkId: Int): Drink? {
        return withContext(dispatchersProvider.io) {
            drinkCacheDao.getDrinkById(drinkId)?.toDrink()
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun refreshDrinks(): ApiResult<Unit> {
        return withContext(dispatchersProvider.io) {
            refreshDrinksInternal()
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private suspend fun refreshDrinksInternal(): ApiResult<Unit> {
        val hotResult = ApiManager.execute { apiService.getHotCoffees() }
        val coldResult = ApiManager.execute { apiService.getIcedCoffees() }

        return when {
            hotResult is ApiResult.Success &&
                    coldResult is ApiResult.Success -> {
                val hotEntities = hotResult.data.map { it.toDrinkCacheEntity(isHot = true) }
                val coldEntities = coldResult.data.map { it.toDrinkCacheEntity(isHot = false) }

                drinkCacheDao.clearAll()
                drinkCacheDao.insertDrinks(hotEntities + coldEntities)

                ApiResult.Success(Unit)
            }

            hotResult is ApiResult.Failure -> {
                ApiResult.Failure(hotResult.exception)
            }

            coldResult is ApiResult.Failure -> {
                ApiResult.Failure(coldResult.exception)
            }

            else -> {
                ApiResult.Failure(Exception("Unexpected refresh result"))
            }
        }
    }
}