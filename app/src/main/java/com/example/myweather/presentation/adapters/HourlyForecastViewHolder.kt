package com.example.myweather.presentation.adapters

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.HourlyWeatherItemBinding
import com.example.myweather.domain.entity.HourlyForecast
import com.example.myweather.presentation.mapIconIdToResId
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyForecastViewHolder(
    private val binding: HourlyWeatherItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HourlyForecast) {
        with(binding) {
            val icon =
                ResourcesCompat.getDrawable(root.resources, mapIconIdToResId(item.icon), null)
            weatherIcon.setImageDrawable(icon)
            tvHour.text = convertTimestampToHour(item.datetimeEpoch)
            tvPrecipProb.text = String.format(
                Locale.getDefault(),
                binding.root.resources.getString(R.string.precip_prob_template),
                item.precipProb.roundToInt()
            )
            tvTemp.text = String.format(
                Locale.getDefault(),
                binding.root.resources.getString(R.string.temperature_template),
                item.temp.roundToInt()
            )
        }

    }


    private fun convertTimestampToHour(timestamp: Long): String {
        return SimpleDateFormat("HH:00", Locale.getDefault()).format(timestamp * 1000)
    }
}