package com.example.myweather.data.implementation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.myweather.data.api.ApiFactory
import com.example.myweather.data.db.AppDatabase
import com.example.myweather.data.implementation.exceptions.AuthorizationErrorException
import com.example.myweather.data.implementation.exceptions.ClientErrorException
import com.example.myweather.data.implementation.exceptions.NetworkFailureException
import com.example.myweather.data.implementation.exceptions.ServerErrorException
import com.example.myweather.data.mappers.toDbModel
import com.example.myweather.data.mappers.toEntity
import com.example.myweather.domain.entity.CurrentConditions
import com.example.myweather.domain.entity.DailyForecast
import com.example.myweather.domain.entity.HourlyForecast
import com.example.myweather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(application: Application) : WeatherRepository {
    private val forecastDao = AppDatabase.getInstance(application).forecastDao()
    private val apiService = ApiFactory.apiService

    override fun getTodayHourlyForecast(): LiveData<List<HourlyForecast>> =
        MediatorLiveData<List<HourlyForecast>>().apply {
            addSource(forecastDao.getForecastForToday()) {
                if (it != null) {
                    value = it.map { item -> item.toEntity() }
                }
            }
        }

    override fun getCurrentForecast(): LiveData<CurrentConditions> =
        MediatorLiveData<CurrentConditions>().apply {
            addSource(forecastDao.getCurrentCondition()) {
                if (it != null) {
                    value = it.toEntity()
                }
            }
        }

    override fun getWeeklyForecast(): LiveData<List<DailyForecast>> =
        MediatorLiveData<List<DailyForecast>>().apply {
            addSource(forecastDao.getForecastForWeek()) {
                if (it != null) {
                    value = it.map { item -> item.toEntity() }
                }
            }
        }

    override suspend fun loadData(cityName: String) {

        val todayResponse = apiService.getCurrentAndHourlyForecast(cityName)
        if (!todayResponse.isSuccessful) {
            throw mapHttpCodeToException(todayResponse.code())
        }

        val address = todayResponse.body()?.resolvedAddress ?: ""
        val currentConditions = todayResponse.body()?.currentConditions?.toDbModel(address)
            ?: throw RuntimeException("Current conditions requested but not exists.")

        forecastDao.removeAndInsertCurrentCondition(currentConditions)
        val hourlyForecast = todayResponse.body()?.days?.get(0)?.hours?.map { it.toDbModel() }
            ?: throw RuntimeException("Hourly forecast requested but not exists.")

        forecastDao.removeAndInsertForecastForToday(hourlyForecast)

        val weeklyResponse = apiService.getWeeklyForecast(cityName)

        if(!weeklyResponse.isSuccessful) {
            throw mapHttpCodeToException(weeklyResponse.code())
        }

        val weeklyForecast = weeklyResponse.body()?.days?.map { it.toDbModel() }
            ?: throw RuntimeException("Weekly forecast requested but not exists.")
        forecastDao.removeAndInsertForecastForWeek(weeklyForecast)
    }

    private fun mapHttpCodeToException(code: Int): Exception {
        return when (code) {
            401, 403 -> AuthorizationErrorException()
            in 400..451 -> ClientErrorException()
            in 500..526 -> ServerErrorException()
            else -> NetworkFailureException()
        }
    }
}