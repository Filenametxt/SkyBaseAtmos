package com.example.skybaseatmos.data.remote.service

import com.example.skybaseatmos.BuildConfig
import com.example.skybaseatmos.data.remote.model.RemoteWeather
import retrofit2.http.GET
import retrofit2.http.Query


//https://api.openweathermap.org/data/2.5/weather?
// lat=41.89&lon=12.49&appid=APIK&units=metric&lang=it
interface EndpointService {
    @GET("data/2.5/weather")
    suspend fun forecasts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = BuildConfig.OpenWeather_api,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "it"
    ): RemoteWeather
}
