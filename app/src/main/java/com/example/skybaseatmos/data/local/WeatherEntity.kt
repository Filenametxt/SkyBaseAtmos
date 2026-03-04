package com.example.skybaseatmos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favourites")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val nomeC: String,
    val lat: Double,
    val lon: Double,
    val temp: Double?= null,
    val humidity: Int?= null,
    val weather: String?= null,
    val weatherID: Int?= null
)