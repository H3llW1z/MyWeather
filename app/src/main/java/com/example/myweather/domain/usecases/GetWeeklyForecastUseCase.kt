package com.example.myweather.domain.usecases

import com.example.myweather.domain.entity.DailyForecast
import com.example.myweather.domain.repository.WeatherRepository

class GetWeeklyForecastUseCase(private val repository: WeatherRepository) {
    operator fun invoke(): List<DailyForecast> {
        return repository.getWeeklyForecast()
    }
}