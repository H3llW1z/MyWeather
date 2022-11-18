package com.example.myweather.data.api

import com.example.myweather.data.api.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface ApiService {

    @GET("{location}/today?$QUERY_PARAM_INCLUDE=$HOURS%2C$CURRENT")
    suspend fun getCurrentAndHourlyForecast(
        @Path("location") location: String,
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_UNIT_GROUP) unitGroup: String = UNIT_GROUP,
        @Query(QUERY_PARAM_LANGUAGE) language: String = Locale.getDefault().language
    ): Response<WeatherResponse>

    @GET("{location}/next7days?$QUERY_PARAM_INCLUDE=$DAYS")
    suspend fun getWeeklyForecast(
        @Path("location") location: String,
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_UNIT_GROUP) unitGroup: String = UNIT_GROUP,
        @Query(QUERY_PARAM_LANGUAGE) language: String = Locale.getDefault().language
    ): Response<WeatherResponse>

    companion object {
        private const val QUERY_PARAM_API_KEY = "key"
        private const val API_KEY = "CXF8BUG4HBESW2E8ETVMFZXXF"
        private const val QUERY_PARAM_INCLUDE = "include"
        private const val QUERY_PARAM_LANGUAGE = "lang"
        private const val QUERY_PARAM_UNIT_GROUP = "unitGroup"

        private const val HOURS = "hours"
        private const val CURRENT = "current"
        private const val DAYS = "days"
        private const val UNIT_GROUP = "metric"
    }
}