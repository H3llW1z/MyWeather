package com.example.myweather.domain.entity

data class CurrentConditions(
    val datetimeEpoch: Long,
    val address: String,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Double,
    val precip: Double,
    val precipProb: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double,
    val uvIndex: Double,
    val conditions: String,
    val icon: String,
    val sunrise: String,
    val sunset: String,
)
