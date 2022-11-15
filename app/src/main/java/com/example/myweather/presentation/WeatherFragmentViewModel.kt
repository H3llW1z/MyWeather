package com.example.myweather.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.implementation.WeatherRepositoryImpl
import com.example.myweather.domain.usecases.GetCurrentForecastUseCase
import com.example.myweather.domain.usecases.GetTodayHourlyForecastUseCase
import com.example.myweather.domain.usecases.GetWeeklyForecastUseCase
import com.example.myweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.launch

class WeatherFragmentViewModel(application: Application): AndroidViewModel(application) {
    private val repository = WeatherRepositoryImpl(application)

    private val loadDataUseCase = LoadDataUseCase(repository)
    private val getCurrentForecastUseCase = GetCurrentForecastUseCase(repository)
    private val getHourlyForecastUseCase = GetTodayHourlyForecastUseCase(repository)
    private val getWeeklyForecastUseCase = GetWeeklyForecastUseCase(repository)

    val currentConditions = getCurrentForecastUseCase()
    val hourlyForecast = getHourlyForecastUseCase()
    val weeklyForecast = getWeeklyForecastUseCase()

    fun loadData(cityName: String) {
        viewModelScope.launch {
            loadDataUseCase(cityName)
        }
    }

}