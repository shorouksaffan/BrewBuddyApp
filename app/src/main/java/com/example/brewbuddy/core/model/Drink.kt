package com.example.brewbuddy.core.model

data class Drink(
    val id: Int,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val price: Money,
    val isHot: Boolean
) {
    val category: String
        get() = if (isHot) "Hot" else "Cold"

    val ingredientsText: String
        get() = ingredients.joinToString(", ")
}