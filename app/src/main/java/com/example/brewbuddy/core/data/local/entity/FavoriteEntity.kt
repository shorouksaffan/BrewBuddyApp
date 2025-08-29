package com.example.brewbuddy.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val drinkId: Int,
    val addedAt: Long = System.currentTimeMillis()
)
