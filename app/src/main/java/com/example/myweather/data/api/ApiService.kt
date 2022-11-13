package com.example.myweather.data.api

import com.example.myweather.data.api.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("{cityName}}/today?$QUERY_PARAM_INCLUDE=$HOURS%2C$CURRENT")
    suspend fun getCurrentAndHourlyForecast(
        @Path("cityName") cityName: String,
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_UNIT_GROUP) unitGroup: String = UNIT_GROUP,
        @Query(QUERY_PARAM_LANGUAGE) language: String = LANGUAGE
    ): WeatherResponse

    @GET("{cityName}/next7days?$QUERY_PARAM_INCLUDE=$DAYS")
    suspend fun getWeeklyForecast(
        @Path("cityName") cityName: String,
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_UNIT_GROUP) unitGroup: String = UNIT_GROUP,
        @Query(QUERY_PARAM_LANGUAGE) language: String = LANGUAGE
    ): WeatherResponse

    companion object {
        private const val QUERY_PARAM_API_KEY = "key"
        private const val API_KEY = "CXF8BUG4HBESW2E8ETVMFZXXF"
        private const val QUERY_PARAM_INCLUDE = "include"
        private const val QUERY_PARAM_LANGUAGE = "lang"
        private const val QUERY_PARAM_UNIT_GROUP = "unitGroup"

        private const val HOURS = "hours"
        private const val CURRENT = "current"
        private const val DAYS = "days"

        private const val LANGUAGE = "ru"
        private const val UNIT_GROUP = "metric"
    }
}