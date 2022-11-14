package com.example.myweather.domain.repository

import androidx.lifecycle.LiveData
import com.example.myweather.domain.entity.CurrentConditions
import com.example.myweather.domain.entity.DailyForecast
import com.example.myweather.domain.entity.HourlyForecast

interface WeatherRepository {

    fun getTodayHourlyForecast(): LiveData<List<HourlyForecast>>

    fun getCurrentForecast(): LiveData<CurrentConditions>

    fun getWeeklyForecast(): LiveData<List<DailyForecast>>

    suspend fun loadData(cityName: String)
}