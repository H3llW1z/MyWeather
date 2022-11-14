package com.example.myweather.domain.usecases

import androidx.lifecycle.LiveData
import com.example.myweather.domain.entity.CurrentConditions
import com.example.myweather.domain.repository.WeatherRepository

class GetCurrentForecastUseCase(private val repository: WeatherRepository) {
    operator fun invoke(): LiveData<CurrentConditions> {
        return repository.getCurrentForecast()
    }
}