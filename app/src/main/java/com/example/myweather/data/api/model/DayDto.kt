package com.example.myweather.data.api.model

import com.google.gson.annotations.SerializedName


data class DayDto (
    @SerializedName("datetime")
    val datetime: String? = null,

    @SerializedName("datetimeEpoch")
    val datetimeEpoch: Int = 0,

    @SerializedName("tempmax")
    val tempMax: Double = 0.0,

    @SerializedName("tempmin")
    val tempMin: Double = 0.0,

    @SerializedName("temp")
    val temp: Double = 0.0,

    @SerializedName("feelslikemax")
    val feelsLikeMax: Double = 0.0,

    @SerializedName("feelslikemin")
    val feelsLikeMin: Double = 0.0,

    @SerializedName("feelslike")
    val feelsLike: Double = 0.0,

    @SerializedName("dew")
    val dew: Double = 0.0,

    @SerializedName("humidity")
    val humidity: Double = 0.0,

    @SerializedName("precip")
    val precip: Double = 0.0,

    @SerializedName("precipprob")
    val precipProb: Double = 0.0,

    @SerializedName("precipcover")
    val precipCover: Double = 0.0,

    @SerializedName("windspeed")
    val windSpeed: Double = 0.0,

    @SerializedName("winddir")
    val windDir: Double = 0.0,

    @SerializedName("pressure")
    val pressure: Double = 0.0,

    @SerializedName("cloudcover")
    val cloudCover: Double = 0.0,

    @SerializedName("visibility")
    val visibility: Double = 0.0,

    @SerializedName("uvindex")
    val uvIndex: Double = 0.0,

    @SerializedName("sunrise")
    val sunrise: String? = null,

    @SerializedName("sunset")
    val sunset: String? = null,

    @SerializedName("moonphase")
    val moonPhase: Double = 0.0,

    @SerializedName("conditions")
    val conditions: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("icon")
    val icon: String? = null,

    @SerializedName("hours")
    val hours: List<HourDto>? = null
)