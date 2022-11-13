package com.example.myweather.data.api.model

import com.google.gson.annotations.SerializedName




data class WeatherResponse (
    @SerializedName("resolvedAddress")
    val resolvedAddress: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("days")
    val days: List<DayDto>? = null,

    @SerializedName("currentConditions")
    val currentConditions: CurrentConditionsDto? = null
)