package com.example.skybaseatmos.domain.repositories

import com.example.skybaseatmos.domain.model.Weather

interface LocalRepository {
    suspend fun save(weather: Weather)
    suspend fun getAll(): List<Weather>
    suspend fun clear()
    suspend fun check(weather: Weather): Boolean
    suspend fun remove(weather: Weather)
}