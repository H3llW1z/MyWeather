package com.example.myweather.data.implementation

import android.app.Application
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
                if (it !=null) {
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

        try {
            val todayResponse = apiService.getCurrentAndHourlyForecast(cityName)
            if (todayResponse.isSuccessful) {
                val currentConditions = todayResponse.body()?.currentConditions?.toDbModel(cityName)
                    ?: throw RuntimeException("Current conditions requested but not exists.")

                forecastDao.removeAndInsertCurrentCondition(currentConditions)

                val hourlyForecast = todayResponse.body()?.days?.get(0)?.hours?.map { it.toDbModel() }
                    ?: throw RuntimeException("Hourly forecast requested but not exists.")

                forecastDao.removeAndInsertForecastForToday(hourlyForecast)
            }
        } catch (e: Exception){
            Log.e("REPO_ERROR", e.message ?: "no message")
        }

        try {
            val weeklyResponse = apiService.getWeeklyForecast(cityName)
            if (weeklyResponse.isSuccessful) {
                val weeklyForecast = weeklyResponse.body()?.days?.map { it.toDbModel() }
                    ?: throw RuntimeException("Weekly forecast requested but not exists.")
                forecastDao.removeAndInsertForecastForWeek(weeklyForecast)
            }
        } catch (e: Exception) {
            Log.e("REPO_ERROR", e.message ?: "no message")
        }
    }
}