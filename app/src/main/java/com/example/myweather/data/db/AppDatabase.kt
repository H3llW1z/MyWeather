package com.example.myweather.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myweather.data.db.model.CurrentConditionsDbModel
import com.example.myweather.data.db.model.DayForecastDbModel
import com.example.myweather.data.db.model.HourForecastDbModel

@Database(
    entities = [CurrentConditionsDbModel::class, DayForecastDbModel::class, HourForecastDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DATABASE_NAME = "main.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}