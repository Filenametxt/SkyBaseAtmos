package com.example.skybaseatmos.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.skybaseatmos.data.local.WeatherEntity
import com.example.skybaseatmos.domain.model.Weather

@Dao
interface WeatherDAO {
    @Upsert
    suspend fun insert(weather: WeatherEntity)
    @Query("SELECT * FROM Favourites")
    suspend fun getAll(): List<WeatherEntity>
    @Query("DELETE FROM Favourites")
    suspend fun clear()

    @Query("DELETE FROM Favourites WHERE id = :weatherPK")
    suspend fun delete(weatherPK: Long)


}