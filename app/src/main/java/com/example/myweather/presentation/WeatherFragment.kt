package com.example.myweather.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.presentation.adapters.DailyForecastAdapter
import com.example.myweather.presentation.adapters.HourlyForecastAdapter
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentWeatherBinding is null")

    private val hourlyAdapter by lazy {
        HourlyForecastAdapter()
    }

    private val dailyAdapter by lazy {
        DailyForecastAdapter()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[WeatherFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvHourlyForecast.adapter = hourlyAdapter
        binding.rvDailyForecast.adapter = dailyAdapter
        prepareObservers()
        viewModel.loadData("Санкт-Петербург")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun prepareObservers() {

        viewModel.currentConditions.observe(viewLifecycleOwner) {
            with(binding) {
                tvCityName.text = it.address.substringBefore(",")
                tvDate.text = convertTimestampToStringDate(it.datetimeEpoch)

                val icon = ResourcesCompat.getDrawable(resources, mapIconIdToResId(it.icon), null)
                ivIcon.setImageDrawable(icon)
                tvCurrentTemp.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.temperature_template),
                    it.temp.roundToInt()
                )

                tvFeelsLikeTemp.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.feels_like_temperature_template),
                    it.feelsLike.roundToInt()
                )

                tvDescription.text = it.conditions

                val lastUpdateTime = convertTimestampToStringLastUpdated(it.datetimeEpoch)

                tvLastUpdated.text = String.format(
                    Locale.getDefault(),
                    getString(R.string.last_update_time),
                    lastUpdateTime
                )
                tvHumidity.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.humidity_template),
                    it.humidity.roundToInt()
                )

                tvPressure.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.pressure_template),
                    it.pressure.roundToInt()
                )

                tvWindSpeed.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.wind_speed_template),
                    it.windSpeed
                )
                tvPrecip.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.precip_count_template),
                    it.precip
                )
                tvPrecipProb.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.precip_prob_template),
                    it.precipProb.roundToInt()
                )
                tvVisibility.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.visibility_template),
                    it.visibility
                )
                tvUvIndex.text = it.uvIndex.toString()
                tvSunriseTime.text = it.sunrise
                tvSunsetTime.text = it.sunset
            }
        }

        viewModel.hourlyForecast.observe(viewLifecycleOwner) {
            hourlyAdapter.submitList(it)
        }

        viewModel.weeklyForecast.observe(viewLifecycleOwner) {
            dailyAdapter.submitList(it)
        }

        viewModel.clientError.observe(viewLifecycleOwner) {
            showBadCitySnackBar()
        }

        viewModel.networkError.observe(viewLifecycleOwner) {
            showNoInternetSnackBar()
        }

        viewModel.serverError.observe(viewLifecycleOwner) {
            showServerErrorSnackBar()
        }

    }

    private fun showBadCitySnackBar() {
        val snackBar = Snackbar.make(
            binding.root,
            "Неизвестная локация. Проверьте название города",
            Snackbar.LENGTH_LONG
        )
        snackBar.show()
    }

    private fun showNoInternetSnackBar() {
        val snackBar = Snackbar.make(
            binding.root,
            getString(R.string.check_network_connection),
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(getString(R.string.try_again)) {
            viewModel.loadData("Санкт-Петербург")
        }
        snackBar.show()
    }

    private fun showServerErrorSnackBar() {
        val snackBar = Snackbar.make(
            binding.root,
            getString(R.string.server_error_occured),
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(getString(R.string.try_again)) {
            viewModel.loadData("Санкт-Петербург")
        }
        snackBar.show()
    }

    private fun convertTimestampToStringDate(time: Long): String {
        return SimpleDateFormat("d MMMM, EEEE", Locale.getDefault()).format(time * 1000)
    }

    private fun convertTimestampToStringLastUpdated(timestamp: Long): String {
        return SimpleDateFormat("d/MM H:mm", Locale.getDefault()).format(timestamp * 1000)
    }

    companion object {
        fun newInstance() = WeatherFragment()
    }
}