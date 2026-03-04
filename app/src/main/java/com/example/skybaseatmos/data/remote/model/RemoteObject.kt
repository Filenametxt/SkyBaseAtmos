package com.example.skybaseatmos.data.remote.model

/*
{
  "coord": {
    "lon": -0.1257,
    "lat": 51.5085
  },
  "weather": [
    {
      "id": 801,
      "main": "Clouds",
      "description": "poche nuvole",
      "icon": "02n"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 11.3,
    "feels_like": 10.68,
    "temp_min": 10.03,
    "temp_max": 11.97,
    "pressure": 1010,
    "humidity": 84,
    "sea_level": 1010,
    "grnd_level": 1005
  },
  "visibility": 10000,
  "wind": {
    "speed": 4.63,
    "deg": 220
  },
  "clouds": {
    "all": 20
  },
  "dt": 1772218633,
  "sys": {
    "type": 2,
    "id": 2091269,
    "country": "GB",
    "sunrise": 1772175036,
    "sunset": 1772213775
  },
  "timezone": 0,
  "id": 2643743,
  "name": "London",
  "cod": 200
}
 */


data class RemoteWeather(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: String,
    val lat: String
)
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)
data class Wind(
    val speed: Double,
    val deg: Int
)

data class Clouds(
    val all: Int
)
data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)
