package com.example.myweather.data.api.model

import com.google.gson.annotations.SerializedName




data class HourDto (
    @SerializedName("datetime")
    var datetime: String? = null,

    @SerializedName("datetimeEpoch")
    var datetimeEpoch: Int = 0,

    @SerializedName("temp")
    var temp:Double = 0.0,

    @SerializedName("feelslike")
    var feelsLike:Double = 0.0,

    @SerializedName("humidity")
    var humidity:Double = 0.0,

    @SerializedName("dew")
    var dew:Double = 0.0,

    @SerializedName("precip")
    var precip:Double = 0.0,

    @SerializedName("precipprob")
    var precipProb:Double = 0.0,

    @SerializedName("windspeed")
    var windSpeed:Double = 0.0,

    @SerializedName("winddir")
    var windDir:Double = 0.0,

    @SerializedName("pressure")
    var pressure:Double = 0.0,

    @SerializedName("visibility")
    var visibility:Double = 0.0,

    @SerializedName("cloudcover")
    var cloudCover:Double = 0.0,

    @SerializedName("uvindex")
    var uvIndex:Double = 0.0,

    @SerializedName("conditions")
    var conditions: String? = null,

    @SerializedName("icon")
    var icon: String? = null
)