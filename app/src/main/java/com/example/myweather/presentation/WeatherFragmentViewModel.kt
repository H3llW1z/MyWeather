package com.example.myweather.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.implementation.WeatherRepositoryImpl
import com.example.myweather.data.implementation.exceptions.AuthorizationErrorException
import com.example.myweather.data.implementation.exceptions.ClientErrorException
import com.example.myweather.data.implementation.exceptions.NetworkFailureException
import com.example.myweather.data.implementation.exceptions.ServerErrorException
import com.example.myweather.domain.usecases.GetCurrentForecastUseCase
import com.example.myweather.domain.usecases.GetTodayHourlyForecastUseCase
import com.example.myweather.domain.usecases.GetWeeklyForecastUseCase
import com.example.myweather.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class WeatherFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepositoryImpl(application)

    private val loadDataUseCase = LoadDataUseCase(repository)
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

    private val _authError = MutableLiveData<Unit>()
    val authError: LiveData<Unit>
        get() = _authError

    fun loadData(cityName: String) {
        viewModelScope.launch {
            try {
                loadDataUseCase(cityName)
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

}