package com.example.brewbuddy.core.di

import com.example.brewbuddy.core.data.repository.DrinkRepository
import com.example.brewbuddy.core.data.repository.FavoritesRepository
import com.example.brewbuddy.core.data.repository.OrdersRepository
import com.example.brewbuddy.core.data.repository.impl.DrinkRepositoryImpl
import com.example.brewbuddy.core.data.repository.impl.FavoritesRepositoryImpl
import com.example.brewbuddy.core.data.repository.impl.OrdersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDrinkRepository(
        drinkRepositoryImpl: DrinkRepositoryImpl
    ): DrinkRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindOrdersRepository(
        ordersRepositoryImpl: OrdersRepositoryImpl
    ): OrdersRepository
}