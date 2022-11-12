package com.example.myweather.domain.repository

import com.example.myweather.domain.entity.CurrentConditions
import com.example.myweather.domain.entity.DailyForecast
import com.example.myweather.domain.entity.HourlyForecast

interface WeatherRepository {

    fun getTodayHourlyForecast(): List<HourlyForecast>

    fun getCurrentForecast(): CurrentConditions

    fun getWeeklyForecast(): List<DailyForecast>

    fun loadData(cityName: String)
}