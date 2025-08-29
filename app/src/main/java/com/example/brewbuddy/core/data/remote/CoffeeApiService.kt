package com.example.brewbuddy.core.data.remote

import com.example.brewbuddy.core.data.remote.dto.CoffeeDto
import retrofit2.http.GET

interface CoffeeApiService {
    @GET("hot")
    suspend fun getHotCoffees(): List<CoffeeDto>

    @GET("iced")
    suspend fun getIcedCoffees(): List<CoffeeDto>
}