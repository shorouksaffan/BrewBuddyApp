package com.example.brewbuddy.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.brewbuddy.core.data.local.dao.DrinkCacheDao
import com.example.brewbuddy.core.data.local.dao.FavoriteDao
import com.example.brewbuddy.core.data.local.dao.OrderDao
import com.example.brewbuddy.core.data.local.entity.DrinkCacheEntity
import com.example.brewbuddy.core.data.local.entity.FavoriteEntity
import com.example.brewbuddy.core.data.local.entity.OrderEntity
import com.example.brewbuddy.core.data.local.entity.OrderItemEntity

@Database(
    entities = [
        DrinkCacheEntity::class,
        FavoriteEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalTypeConverters::class)
abstract class BrewBuddyDatabase : RoomDatabase() {
    abstract fun drinkCacheDao(): DrinkCacheDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: BrewBuddyDatabase? = null
        fun getInstance(context: Context): BrewBuddyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BrewBuddyDatabase::class.java,
                    "brewbuddy_database"
                ).fallbackToDestructiveMigration(false).build().also { INSTANCE = it }
            }
        }
    }
}