package com.example.skybaseatmos.domain.model

data class Weather(
    val id: Long,   //id della città
    val nomeC: String,
    val lat: Double,
    val lon: Double,
    val temp: Double?= null,
    val humidity: Int?= null,
    val weather: String?= null,
    val weatherID: Int?= null
) {
    fun orNull(): Weather {
        return Weather(
            id = id,
            nomeC = nomeC,
            lat = lat,
            lon = lon,
            temp = temp,
            humidity = humidity,
            weather = weather,
            weatherID = weatherID
        )
    }

    override fun toString(): String {
        return "Weather(id=$id, nomeC='$nomeC', lat=$lat, lon=$lon, temp=$temp, humidity=$humidity, weather=$weather, weatherID=$weatherID)"
    }
}
