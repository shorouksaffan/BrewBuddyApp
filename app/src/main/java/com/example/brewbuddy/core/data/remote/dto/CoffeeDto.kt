package com.example.brewbuddy.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CoffeeDto(
    val description: String,
    val id: Int,
    @SerializedName("image")
    val imageUrl: String,
    val ingredients: List<String>,
    val title: String
)