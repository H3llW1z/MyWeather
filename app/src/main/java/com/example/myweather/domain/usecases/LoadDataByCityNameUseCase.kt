package com.example.myweather.domain.usecases

import com.example.myweather.domain.repository.WeatherRepository

class LoadDataByCityNameUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(cityName: String) {
        repository.loadDataByCityName(cityName)
    }
}