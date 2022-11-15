package com.example.myweather.domain.entity

data class HourlyForecast (
    val datetime: String,
    val datetimeEpoch: Long,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Double,
    val dew: Double,
    val precip: Double,
    val precipProb: Double,
    val windSpeed: Double,
    val windDir: Double,
    val pressure: Double,
    val visibility: Double,
    val cloudCover: Double,
    val uvIndex: Double,
    val conditions: String,
    val icon: String
)