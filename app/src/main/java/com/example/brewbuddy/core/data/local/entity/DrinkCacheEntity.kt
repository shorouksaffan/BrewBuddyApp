package com.example.brewbuddy.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drink_cache")
data class DrinkCacheEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val isHot: Boolean,
    val cachedAt: Long = System.currentTimeMillis()
)