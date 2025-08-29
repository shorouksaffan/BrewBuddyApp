package com.example.brewbuddy.core.data.remote.dto

import com.example.brewbuddy.core.data.local.entity.DrinkCacheEntity
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.util.PriceCatalog

fun CoffeeDto.toDrinkCacheEntity(isHot: Boolean): DrinkCacheEntity {
    return DrinkCacheEntity(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        imageUrl = imageUrl,
        isHot = isHot
    )
}

fun CoffeeDto.toDrink(isHot: Boolean): Drink {
    return Drink(
        id = id,
        name = title,
        description = description,
        ingredients = ingredients,
        imageUrl = imageUrl,
        price = PriceCatalog.getPriceForDrink(title),
        isHot = isHot
    )
}