package com.example.myweather.presentation.adapters

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.DailyWeatherItemBinding
import com.example.myweather.domain.entity.DailyForecast
import com.example.myweather.presentation.mapIconIdToResId
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyForecastViewHolder(
    private val binding: DailyWeatherItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DailyForecast) {
        with(binding) {
            val icon =
                ResourcesCompat.getDrawable(root.resources, mapIconIdToResId(item.icon), null)
            ivWeatherIcon.setImageDrawable(icon)
            tvDescription.isSelected = true
            tvDayOfWeek.text = convertTimestampToDayOfWeek(item.datetimeEpoch)
            tvDescription.text = item.description
            tvDayNightTemp.text = String.format(
                Locale.getDefault(),
                binding.root.resources.getString(R.string.day_night_temperature_template),
                item.tempMin.roundToInt(),
                item.tempMax.roundToInt()
            )
        }
    }

    private fun convertTimestampToDayOfWeek(timestamp: Long): String {
        return SimpleDateFormat("EE", Locale.getDefault()).format(timestamp * 1000)
    }
}