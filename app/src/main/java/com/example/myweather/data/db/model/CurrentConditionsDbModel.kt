package com.example.myweather.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_condition")
data class CurrentConditionsDbModel(
    @PrimaryKey(autoGenerate = false)
    val datetimeEpoch: Long,
    val address: String,
    val datetime: String,
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
    val icon: String,
    val sunrise: String,
    val sunset: String,
    val moonPhase: Double
)