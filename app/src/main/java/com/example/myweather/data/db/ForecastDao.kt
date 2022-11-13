package com.example.myweather.data.db

import androidx.room.Transaction
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myweather.data.db.model.CurrentConditionsDbModel
import com.example.myweather.data.db.model.DayForecastDbModel
import com.example.myweather.data.db.model.HourForecastDbModel

@Dao
interface ForecastDao {
    @Query("SELECT * FROM current_condition LIMIT 1")
    fun getCurrentCondition(): CurrentConditionsDbModel

    @Query("SELECT * FROM hour_forecast ORDER BY datetimeEpoch ASC")
    fun getForecastForToday(): List<HourForecastDbModel>

    @Query("SELECT * FROM day_forecast ORDER BY datetimeEpoch ASC")
    fun getForecastForWeek(): List<DayForecastDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentCondition(data: CurrentConditionsDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastForToday(data: List<HourForecastDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastForWeek(data: List<DayForecastDbModel>)

    @Query("DELETE FROM current_condition")
    suspend fun removeOldCurrentCondition()

    @Query("DELETE FROM hour_forecast")
    suspend fun removeOldForecastForToday()

    @Query("DELETE FROM day_forecast")
    suspend fun removeOldForecastForWeek()

    @Transaction
    suspend fun removeAndInsertCurrentCondition(data: CurrentConditionsDbModel) {
        removeOldCurrentCondition()
        insertCurrentCondition(data)
    }

    @Transaction
    suspend fun removeAndInsertForecastForWeek(data: List<DayForecastDbModel>) {
        removeOldForecastForWeek()
        insertForecastForWeek(data)
    }

    @Transaction
    suspend fun removeAndInsertForecastForToday(data: List<HourForecastDbModel>) {
        removeOldForecastForToday()
        insertForecastForToday(data)
    }
}