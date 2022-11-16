package com.example.myweather.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.myweather.domain.entity.DailyForecast

class DailyForecastDiffCallback: DiffUtil.ItemCallback<DailyForecast>() {
    override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean =
        oldItem.datetimeEpoch == newItem.datetimeEpoch

    override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean =
        oldItem == newItem
}