package com.example.brewbuddy.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.brewbuddy.core.data.local.entity.DrinkCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkCacheDao {

    @Query("SELECT * FROM drink_cache WHERE isHot = :isHot")
    fun getDrinksByType(isHot: Boolean): Flow<List<DrinkCacheEntity>>

    @Query("SELECT * FROM drink_cache")
    fun getAllDrinks(): Flow<List<DrinkCacheEntity>>

    @Query("SELECT * FROM drink_cache WHERE id = :drinkId")
    suspend fun getDrinkById(drinkId: Int): DrinkCacheEntity?

    @Query("SELECT * FROM drink_cache WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchDrinks(query: String): Flow<List<DrinkCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinks(drinks: List<DrinkCacheEntity>)

    @Query("DELETE FROM drink_cache")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM drink_cache")
    suspend fun getCount(): Int
}