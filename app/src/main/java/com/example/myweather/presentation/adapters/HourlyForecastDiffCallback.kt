package com.example.myweather.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.myweather.domain.entity.HourlyForecast

class HourlyForecastDiffCallback: DiffUtil.ItemCallback<HourlyForecast>() {
    override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
        return oldItem.datetimeEpoch == newItem.datetimeEpoch
    }

    override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
        return oldItem == newItem
    }
}