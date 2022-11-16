package com.example.myweather.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myweather.databinding.DailyWeatherItemBinding
import com.example.myweather.domain.entity.DailyForecast

class DailyForecastAdapter :
    ListAdapter<DailyForecast, DailyForecastViewHolder>(DailyForecastDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding =
            DailyWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        val weatherItem = currentList[position]
        holder.bind(weatherItem)
    }
}