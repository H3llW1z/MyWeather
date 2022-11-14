package com.example.myweather.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myweather.R
import com.example.myweather.data.implementation.WeatherRepositoryImpl
import com.example.myweather.domain.usecases.GetCurrentForecastUseCase
import com.example.myweather.domain.usecases.GetTodayHourlyForecastUseCase
import com.example.myweather.domain.usecases.GetWeeklyForecastUseCase
import com.example.myweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}