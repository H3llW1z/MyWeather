package com.example.myweather.data.implementation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.myweather.data.api.ApiFactory
import com.example.myweather.data.db.AppDatabase
import com.example.myweather.data.mappers.ForecastMappers.Companion.toDbModel
import com.example.myweather.data.mappers.ForecastMappers.Companion.toEntity
import com.example.myweather.domain.entity.CurrentConditions
import com.example.myweather.domain.entity.DailyForecast
import com.example.myweather.domain.entity.HourlyForecast
import com.example.myweather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(application: Application) : WeatherRepository {
    private val forecastDao = AppDatabase.getInstance(application).forecastDao()
    private val apiService = ApiFactory.apiService

    override fun getTodayHourlyForecast(): LiveData<List<HourlyForecast>> =
        Transformations.map(
            forecastDao.getForecastForToday()
        ) {
            it?.map { item -> item.toEntity() }
        }

    override fun getCurrentForecast(): LiveData<CurrentConditions> =
        Transformations.map(
            forecastDao.getCurrentCondition()
        ) {
            it?.toEntity()
        }

    override fun getWeeklyForecast(): LiveData<List<DailyForecast>> =
        Transformations.map(
            forecastDao.getForecastForWeek()
        ) {
            it?.map { item -> item.toEntity() }
        }

    override suspend fun loadData(cityName: String) {
        val weeklyResponse = apiService.getWeeklyForecast(cityName)
        val todayResponse = apiService.getCurrentAndHourlyForecast(cityName)

        val currentConditions = todayResponse.currentConditions?.toDbModel(cityName)
            ?: throw RuntimeException("Current conditions requested but not exists.")

        val hourlyForecast = todayResponse.days?.get(0)?.hours?.map { it.toDbModel() }
            ?: throw RuntimeException("Hourly forecast requested but not exists.")

        val weeklyForecast = weeklyResponse.days?.map { it.toDbModel() }
            ?: throw RuntimeException("Weekly forecast requested but not exists.")

        forecastDao.removeAndInsertCurrentCondition(currentConditions)
        forecastDao.removeAndInsertForecastForWeek(weeklyForecast)
        forecastDao.removeAndInsertForecastForToday(hourlyForecast)
    }
}