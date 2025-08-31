package com.example.brewbuddy.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drink(
    val id: Int,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val price: Money,
    val isHot: Boolean
) : Parcelable {
    val category: String
        get() = if (isHot) "Hot" else "Cold"

    val ingredientsText: String
        get() = ingredients.joinToString(", ")
}