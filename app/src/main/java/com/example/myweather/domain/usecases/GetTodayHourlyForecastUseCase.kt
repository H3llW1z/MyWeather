package com.example.myweather.domain.usecases

import androidx.lifecycle.LiveData
import com.example.myweather.domain.entity.HourlyForecast
import com.example.myweather.domain.repository.WeatherRepository

class GetTodayHourlyForecastUseCase(private val repository: WeatherRepository) {
    operator fun invoke(): LiveData<List<HourlyForecast>> {
        return repository.getTodayHourlyForecast()
    }
}