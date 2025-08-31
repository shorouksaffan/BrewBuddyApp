package com.example.brewbuddy.core.data.remote

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val exception: Exception) : ApiResult<Nothing>()

    object Loading : ApiResult<Nothing>()

}