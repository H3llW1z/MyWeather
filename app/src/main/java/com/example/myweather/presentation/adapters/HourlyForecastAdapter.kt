package com.example.myweather.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myweather.databinding.HourlyWeatherItemBinding
import com.example.myweather.domain.entity.HourlyForecast

class HourlyForecastAdapter: ListAdapter<HourlyForecast, HourlyForecastViewHolder>(
    HourlyForecastDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val binding = HourlyWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val weatherItem = currentList[position]
        holder.bind(weatherItem)
    }
}