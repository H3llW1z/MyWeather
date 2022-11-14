package com.example.myweather.domain.usecases

import com.example.myweather.domain.repository.WeatherRepository

class LoadDataUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(cityName: String) {
        repository.loadData(cityName)
    }
}