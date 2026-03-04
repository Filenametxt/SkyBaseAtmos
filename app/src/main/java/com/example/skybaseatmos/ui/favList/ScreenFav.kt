package com.example.skybaseatmos.ui.favList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.ui.forecast.weatherIcon

@Composable
fun ScreenFav(viewModel: ScreenFavViewModel = hiltViewModel(), onItemClick: (Weather) -> Unit = {}) {
    val uiState=viewModel.uiState
    ListContent(
        items = uiState.items,
        onItemClick = onItemClick
    )
}

@Composable
fun ListContent(items: List<Weather>, onItemClick: (Weather) -> Unit = {}) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nessun elemento trovato")
        }
    }
    else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items.size) { index ->
                ListItem(
                    icon = weatherIcon(items[index].weatherID),
                    nameC = items[index].nomeC,
                    temperature = items[index].temp,
                    weather = items[index].weather
                )
            }
        }
    }
}
@Composable
fun ListItem(icon: ImageVector, nameC: String, temperature: Double?,weather: String?){
    Row {
        Icon(
            imageVector = icon,
            contentDescription = weather,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(text = nameC, modifier = Modifier.align(Alignment.CenterVertically))
        Text(text = "$temperature °C", modifier = Modifier.align(Alignment.CenterVertically))

    }
}