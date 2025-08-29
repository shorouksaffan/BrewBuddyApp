package com.example.brewbuddy.core.di

import android.content.Context
import com.example.brewbuddy.core.data.local.BrewBuddyDatabase
import com.example.brewbuddy.core.data.local.dao.DrinkCacheDao
import com.example.brewbuddy.core.data.local.dao.FavoriteDao
import com.example.brewbuddy.core.data.local.dao.OrderDao
import com.example.brewbuddy.core.prefs.UserPrefs
import com.example.brewbuddy.core.util.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BrewBuddyDatabase {
        return BrewBuddyDatabase.getInstance(context)
    }

    @Provides
    fun provideDrinkCacheDao(database: BrewBuddyDatabase): DrinkCacheDao {
        return database.drinkCacheDao()
    }

    @Provides
    fun provideFavoriteDao(database: BrewBuddyDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    fun provideOrderDao(database: BrewBuddyDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    @Singleton
    fun provideUserPrefs(@ApplicationContext context: Context): UserPrefs {
        return UserPrefs(context)
    }

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider {
        return object : DispatchersProvider {
            override val main = Dispatchers.Main
            override val io = Dispatchers.IO
            override val default = Dispatchers.Default
            override val unconfined = Dispatchers.Unconfined
        }
    }
}