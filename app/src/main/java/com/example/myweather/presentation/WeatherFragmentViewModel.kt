package com.example.myweather.presentation

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.implementation.WeatherRepositoryImpl
import com.example.myweather.data.implementation.exceptions.AuthorizationErrorException
import com.example.myweather.data.implementation.exceptions.ClientErrorException
import com.example.myweather.data.implementation.exceptions.NetworkFailureException
import com.example.myweather.data.implementation.exceptions.ServerErrorException
import com.example.myweather.domain.usecases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.util.*

class WeatherFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepositoryImpl(application)

    private val loadDataByCityNameUseCase = LoadDataByCityNameUseCase(repository)
    private val getCurrentForecastUseCase = GetCurrentForecastUseCase(repository)
    private val getHourlyForecastUseCase = GetTodayHourlyForecastUseCase(repository)
    private val getWeeklyForecastUseCase = GetWeeklyForecastUseCase(repository)

    val currentConditions = getCurrentForecastUseCase()
    val hourlyForecast = getHourlyForecastUseCase()
    val weeklyForecast = getWeeklyForecastUseCase()

    private val _clientError = MutableLiveData<Unit>()
    val clientError: LiveData<Unit>
        get() = _clientError

    private val _networkError = MutableLiveData<Unit>()
    val networkError: LiveData<Unit>
        get() = _networkError

    private val _serverError = MutableLiveData<Unit>()
    val serverError: LiveData<Unit>
        get() = _serverError

    fun loadDataByCityName(cityName: String) {
        viewModelScope.launch {
            try {
                loadDataByCityNameUseCase(cityName)
            } catch (e: AuthorizationErrorException) {
                Log.e("REPO_ERROR", "Auth error")
            } catch (e: ClientErrorException) {
                _clientError.postValue(Unit)
                Log.e("REPO_ERROR", "Client error")
            } catch (e: ServerErrorException) {
                Log.e("REPO_ERROR", "Server error")
                _serverError.postValue(Unit)
            } catch (e: NetworkFailureException) {
                Log.e("REPO_ERROR", "Network failure")
                _networkError.postValue(Unit)
            } catch (e: UnknownHostException) {
                Log.e("REPO_ERROR", "Unknown host")
                _networkError.postValue(Unit)
            }
        }
    }

    @Suppress("DEPRECATION")
    fun loadDataByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            try {
                val address = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1,
                    )
                }
                address?.let {
                    if (it.size > 0) {
                        loadDataByCityName(it[0].locality)
                    }
                }
            } catch (e: Exception) {
                _networkError.postValue(Unit)
            }
        }
    }
}