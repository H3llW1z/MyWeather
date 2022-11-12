package com.example.myweather.domain.entity

data class HourlyForecast (
    val datetime: String,
    val datetimeEpoch: Int,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Double,
    val dew: Double,
    val precip: Double,
    val precipProb: Double,
    val snow: Double,
    val snowDepth: Double,
    val precipType: List<String>,
    val windGust: Double,
    val windSpeed: Double,
    val windDir: Double,
    val pressure: Double,
    val visibility: Double,
    val cloudCover: Double,
    val uvIndex: Double,
    val conditions: String,
    val icon: String
)