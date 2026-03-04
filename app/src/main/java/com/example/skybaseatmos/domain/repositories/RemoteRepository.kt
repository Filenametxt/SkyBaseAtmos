package com.example.skybaseatmos.domain.repositories


import com.example.skybaseatmos.domain.model.Weather

interface RemoteRepository {
    suspend fun downloadData(lat: Double, lon: Double): Weather?
}
