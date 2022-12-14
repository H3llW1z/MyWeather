package com.example.myweather.domain.entity

data class DailyForecast(
    val datetime: String,
    val datetimeEpoch: Long,
    val tempMax: Double,
    val tempMin: Double,
    val temp: Double,
    val feelsLikeMax: Double,
    val feelsLikeMin: Double,
    val feelsLike: Double,
    val dew: Double,
    val humidity: Double,
    val precip: Double,
    val precipProb: Double,
    val precipCover: Double,
    val windSpeed: Double,
    val windDir: Double,
    val pressure: Double,
    val cloudCover: Double,
    val visibility: Double,
    val uvIndex: Double,
    val sunrise: String,
    val sunset: String,
    val moonPhase: Double,
    val conditions: String,
    val description: String,
    val icon: String,
)