package com.example.myweather.data.mappers

import com.example.myweather.data.api.model.CurrentConditionsDto
import com.example.myweather.data.api.model.DayDto
import com.example.myweather.data.api.model.HourDto
import com.example.myweather.data.db.model.CurrentConditionsDbModel
import com.example.myweather.data.db.model.DayForecastDbModel
import com.example.myweather.data.db.model.HourForecastDbModel
import com.example.myweather.domain.entity.CurrentConditions
import com.example.myweather.domain.entity.DailyForecast
import com.example.myweather.domain.entity.HourlyForecast

class ForecastMappers {
    companion object {
        fun CurrentConditionsDto.toDbModel(cityName: String): CurrentConditionsDbModel {
            return CurrentConditionsDbModel(
                datetimeEpoch = datetimeEpoch,
                datetime = datetime ?: "unknown",
                temp = temp,
                feelsLike = feelsLike,
                humidity = humidity,
                dew = dew,
                precip = precip,
                precipProb = precipProb,
                windSpeed = windSpeed,
                windDir = windDir,
                pressure = pressure,
                visibility = visibility,
                cloudCover = cloudCover,
                uvIndex = uvIndex,
                conditions = conditions ?: "unknown",
                icon = icon ?: "unknown icon",
                sunrise = sunrise ?: "unknown time",
                sunset = sunset ?: "unknown time",
                moonPhase = moonPhase,
                cityName = cityName
            )
        }

        fun DayDto.toDbModel(): DayForecastDbModel {
            return DayForecastDbModel(
                datetimeEpoch = datetimeEpoch,
                datetime = datetime ?: "unknown",
                tempMax = tempMax,
                tempMin = tempMin,
                temp = temp,
                feelsLikeMax = feelsLikeMax,
                feelsLikeMin = feelsLikeMin,
                feelsLike = feelsLike,
                dew = dew,
                humidity = humidity,
                precip = precip,
                precipProb = precipProb,
                precipCover = precipCover,
                windSpeed = windSpeed,
                windDir = windDir,
                pressure = pressure,
                cloudCover = cloudCover,
                visibility = visibility,
                uvIndex = uvIndex,
                sunrise = sunrise ?: "unknown time",
                sunset = sunset ?: "unknown time",
                moonPhase = moonPhase,
                conditions = conditions ?: "unknown",
                description = description ?: "no description",
                icon = icon ?: "unknown icon"
            )
        }

        fun HourDto.toDbModel(): HourForecastDbModel {
            return HourForecastDbModel(
                datetimeEpoch = datetimeEpoch,
                datetime = datetime ?: "unknown",
                temp = temp,
                feelsLike = feelsLike,
                humidity = humidity,
                dew = dew,
                precip = precip,
                precipProb = precipProb,
                windSpeed = windSpeed,
                windDir = windDir,
                pressure = pressure,
                visibility = visibility,
                cloudCover = cloudCover,
                uvIndex = uvIndex,
                conditions = conditions ?: "unknown",
                icon = icon ?: "unknown icon"
            )
        }

        fun CurrentConditionsDbModel.toEntity(): CurrentConditions {
            return CurrentConditions(
                datetime = datetime,
                datetimeEpoch = datetimeEpoch,
                temp = temp,
                feelsLike = feelsLike,
                humidity = humidity,
                dew = dew,
                precip = precip,
                precipProb = precipProb,
                windSpeed = windSpeed,
                windDir = windDir,
                pressure = pressure,
                visibility = visibility,
                cloudCover = cloudCover,
                uvIndex = uvIndex,
                conditions = conditions,
                icon = icon,
                sunrise = sunrise,
                sunset = sunset,
                moonPhase = moonPhase,
                cityName = cityName
            )
        }

        fun DayForecastDbModel.toEntity(): DailyForecast {
            return DailyForecast(
                datetime = datetime,
                datetimeEpoch = datetimeEpoch,
                tempMax = tempMax,
                tempMin = tempMin,
                temp = temp,
                feelsLike = feelsLike,
                feelsLikeMax = feelsLikeMax,
                feelsLikeMin = feelsLikeMin,
                dew = dew,
                humidity = humidity,
                precip = precip,
                precipProb = precipProb,
                precipCover = precipCover,
                windSpeed = windSpeed,
                windDir = windDir,
                pressure = pressure,
                cloudCover = cloudCover,
                visibility = visibility,
                uvIndex = uvIndex,
                sunrise = sunrise,
                sunset = sunset,
                moonPhase = moonPhase,
                conditions = conditions,
                description = description,
                icon = icon
            )
        }

        fun HourForecastDbModel.toEntity(): HourlyForecast {
            return HourlyForecast(
                datetime = datetime,
                datetimeEpoch = datetimeEpoch,
                temp = temp,
                feelsLike = feelsLike,
                humidity= humidity,
                dew = dew,
                precip = precip,
                precipProb = precipProb,
                windSpeed = windSpeed,
                windDir = windDir,
                pressure = pressure,
                visibility = visibility,
                cloudCover = cloudCover,
                uvIndex = uvIndex,
                conditions = conditions,
                icon = icon
            )
        }
    }
}