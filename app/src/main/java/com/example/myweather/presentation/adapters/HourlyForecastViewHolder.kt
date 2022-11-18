package com.example.myweather.presentation.adapters

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.HourlyWeatherItemBinding
import com.example.myweather.domain.entity.HourlyForecast
import com.example.myweather.presentation.mapIconIdToResId
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
            textViewHour.text = item.datetime.substringBeforeLast(":")
            textViewPrecipProb.text = String.format(
                Locale.getDefault(),
                binding.root.resources.getString(R.string.precip_prob_template),
                item.precipProb.roundToInt()
            )
            textViewTemp.text = String.format(
                Locale.getDefault(),
                binding.root.resources.getString(R.string.temperature_template),
                item.temp.roundToInt()
            )
        }

    }
}