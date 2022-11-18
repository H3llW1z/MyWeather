package com.example.myweather.data.db

import androidx.lifecycle.LiveData
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
    @Query("SELECT * FROM current_conditions LIMIT 1")
    fun getCurrentConditions(): LiveData<CurrentConditionsDbModel>

    @Query("SELECT * FROM hour_forecast ORDER BY datetimeEpoch ASC")
    fun getForecastForToday(): LiveData<List<HourForecastDbModel>>

    @Query("SELECT * FROM day_forecast ORDER BY datetimeEpoch ASC")
    fun getForecastForWeek(): LiveData<List<DayForecastDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentConditions(data: CurrentConditionsDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastForToday(data: List<HourForecastDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastForWeek(data: List<DayForecastDbModel>)

    @Query("DELETE FROM current_conditions")
    suspend fun removeOldCurrentConditions()

    @Query("DELETE FROM hour_forecast")
    suspend fun removeOldForecastForToday()

    @Query("DELETE FROM day_forecast")
    suspend fun removeOldForecastForWeek()

    @Transaction
    suspend fun removeAndInsertCurrentCondition(data: CurrentConditionsDbModel) {
        removeOldCurrentConditions()
        insertCurrentConditions(data)
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