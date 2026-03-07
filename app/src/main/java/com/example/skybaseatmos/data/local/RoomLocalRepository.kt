package com.example.skybaseatmos.data.local

import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.repositories.LocalRepository
import javax.inject.Inject

private fun Weather.toEntity()= WeatherEntity(
    id=id,
    nomeC=nomeC,
    lat=lat,
    lon=lon,
    temp=temp,
    humidity=humidity,
    weather=weather,
    weatherID=weatherID
)

private fun WeatherEntity.toDomain() = Weather(
    id=id,
    nomeC=nomeC,
    lat=lat,
    lon=lon,
    temp=temp,
    humidity=humidity,
    weather=weather,
    weatherID=weatherID
)

class RoomLocalRepository @Inject constructor(private val weatherDao: WeatherDAO) : LocalRepository {
    override suspend fun save(weather: Weather) {
        weatherDao.insert(weather.toEntity())
        //with(Dispatchers.IO) {}
    }
    override suspend fun getAll(): List<Weather> {
        return weatherDao.getAll().map { it.toDomain() }
    }
    override suspend fun clear() {
        weatherDao.clear()
    }
    override suspend fun check(weather: Weather): Boolean {
        return weatherDao.getAll().any { it.id == weather.id }
    }
    override suspend fun remove(weather: Weather) {
        weatherDao.delete(weather.id)
    }
}


