package com.example.skybaseatmos.data.remote


import android.util.Log
import com.example.skybaseatmos.data.remote.model.RemoteWeather
import com.example.skybaseatmos.data.remote.service.EndpointService
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.repositories.RemoteRepository
import javax.inject.Inject

private fun RemoteWeather.toDomain() = Weather(
    id = this.id,
    nomeC = this.name,
    lat = this.coord.lat.toDoubleOrNull() ?: 0.0,
    lon = this.coord.lon.toDoubleOrNull() ?: 0.0,
    temp = this.main.temp,
    humidity = this.main.humidity,
    weather = this.weather.firstOrNull()?.main,
    weatherID = this.weather.firstOrNull()?.id
)

class RetrofitRemoteRepository @Inject constructor(
    private val service: EndpointService
): RemoteRepository {
    override suspend fun downloadData(lat: Double, lon: Double): Weather? {
        // Nota: Dovrai gestire l'API Key in modo sicuro, ad esempio tramite BuildConfig
        // Per ora la lasciamo vuota o la passiamo come parametro se preferisci

        return try {
            val response = service.forecasts(lat = lat, lon = lon)
            response.toDomain()
        } catch (e: Exception) {
            Log.e("RetrofitRemoteRepository", "downloadData: ${e.message}")
            null
        }
    }
}
