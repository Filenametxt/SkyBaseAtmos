package com.example.skybaseatmos.ui.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skybaseatmos.domain.model.Weather

@Composable
fun Brick(title: String? =null, detail: String? =null, img: ImageVector, desc: String? =null){
    Card(modifier = Modifier.padding(10.dp).fillMaxWidth() // Dimensione orizzontale uguale per tutte
    ){
        Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center){
            Icon(
                imageVector = img,
                contentDescription = desc,
                modifier = Modifier.size(32.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(horizontal = 10.dp)){
                if (title != null) {
                    Text(title)
                }
                if (detail != null) {
                    Text(detail)
                }
            }
        }
    }
}

/*
Gruppo,Fenomeno,Range ID

2xx,Thunderstorm,200 - 232
3xx,Drizzle,300 - 321
5xx,Rain,500 - 531
6xx,Snow,600 - 622
7xx,Atmosphere,701 - 781
800,Clear,800
80x,Clouds,801 - 804
 */
    fun weatherIcon(id: Int?): ImageVector {
        return when (id) {
            in 200..232 -> Icons.Default.Thunderstorm
            in 300..321 -> Icons.Default.Grain
            in 500..531 -> Icons.Default.Umbrella
            in 801..804 -> Icons.Default.Cloud
            800 -> Icons.Default.WbSunny
            in 701..781 -> Icons.Default.Reorder
            in 600..622 -> Icons.Default.AcUnit
            else -> Icons.Default.QuestionMark // Default
        }
    }

@Composable
fun  ForecastDetail(weather: Weather) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Brick("Temperatura",weather.temp.toString()+" °C",Icons.Default.Thermostat,"La temperatura è di"+weather.temp.toString())
        Brick("Umidità",weather.humidity.toString()+" %",Icons.Default.WaterDrop,"L'umidità è di"+weather.humidity.toString())
        Brick(weather.nomeC,"lat: "+weather.lat.toString()+"\nLon: "+weather.lon.toString(),Icons.Default.LocationOn,"Città: "+weather.nomeC)
        Brick(weather.weather,null,weatherIcon(weather.weatherID),"è "+weather.weather)

    }

}

//Per il debugging
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ForecastDetailPreview(){
    val wea=Weather(0,"CentralCity",42.3,14.1,13.0,90,"nuvoloso")
    ForecastDetail(wea)
}
