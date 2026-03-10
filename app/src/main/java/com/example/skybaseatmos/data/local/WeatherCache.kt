package com.example.skybaseatmos.data.local

import com.example.skybaseatmos.common.LocationHelper
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.repositories.RemoteRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

private const val CACHE_TIMEOUT = 36000000L  // 1 Ora
//private const val CACHE_TIMEOUT = 5000 //5 sec <DEBUG>
@Singleton
class WeatherCache @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val locationHelper: LocationHelper
) {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var lastUpdate: Long = 0
    var forecast: Weather? = null

    suspend fun validCache() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdate > CACHE_TIMEOUT || forecast == null) {
            val location = getCurrentLocation()
            if (location != null) {
                dataRequest(location.first, location.second, currentTime)
            }
            else{
                throw Exception("Posizione non disponibile")
            }
        }
    }

    private suspend fun getCurrentLocation(): Pair<Double, Double>? {
        return withTimeoutOrNull(5000L) {
            suspendCancellableCoroutine { continuation ->
                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        val loc = result.lastLocation
                        if (loc != null) {
                            locationHelper.stop(this)
                            if (continuation.isActive) {
                                continuation.resume(Pair(loc.latitude, loc.longitude))
                            }
                        }
                    }
                }
                locationHelper.start(callback)
                continuation.invokeOnCancellation { locationHelper.stop(callback) }
            }
        }
    }
    private suspend fun dataRequest(lat: Double, lon: Double, currentTime: Long) {
        try {
            val f = remoteRepository.downloadData(lat, lon)
            forecast = f
            latitude = lat
            longitude = lon
            lastUpdate = currentTime

        } catch (e: Exception) {
            throw Exception("Aggiornamento meteo fallito: ${e.message}")
        }
    }

    suspend fun getWeatherUpdated(): Weather? {
        validCache()
        return forecast
    }

    suspend fun dbUpdate(lat: Double, lon: Double): Weather? {
        return try{ remoteRepository.downloadData(lat,lon)}
        catch (e: Exception){
            null
        }
    }

    fun getCachedLatitude(): Double = latitude
    fun getCachedLongitude(): Double = longitude
}
