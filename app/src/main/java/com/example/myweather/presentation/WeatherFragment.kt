package com.example.myweather.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import java.text.SimpleDateFormat
import java.util.*

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentWeatherBinding is null")

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
        prepareObservers()
        viewModel.loadData("Санкт-Петербург")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun prepareObservers() {
        viewModel.currentConditions.observe(viewLifecycleOwner) {
            with(binding) {
                tvCityName.text = it.address.substringBefore(",")
                tvDate.text = convertTimestampToStringDate(it.datetimeEpoch)

                tvCurrentTemp.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.temperature_template),
                    it.temp.toInt()
                )

                tvFeelsLikeTemp.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.feels_like_temperature_template),
                    it.feelsLike.toInt()
                )

                tvDescription.text = it.conditions

                tvHumidity.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.humidity_template),
                    it.humidity.toInt()
                )

                tvPressure.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.pressure_template),
                    it.pressure.toInt()
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
                    it.precipProb.toInt()
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
    }

    private fun convertTimestampToStringDate(time: Long): String {
        return SimpleDateFormat("d MMMM, EEEE", Locale.getDefault()).format(time * 1000)
    }

    companion object {
        fun newInstance() = WeatherFragment()
    }
}