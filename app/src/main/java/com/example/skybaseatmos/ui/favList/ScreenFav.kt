package com.example.skybaseatmos.ui.favList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.ui.forecast.weatherIcon

@Composable
fun ScreenFav(viewModel: ScreenFavViewModel = hiltViewModel(), onItemClick: (Weather) -> Unit = {}) {
    val uiState=viewModel.uiState
    // Questo farà sì che i dati vengano ricaricati ogni volta che entri in questa schermata
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
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
                Item(
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
fun Item(icon: ImageVector, nameC: String, temperature: Double?,weather: String?){
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        shape = RoundedCornerShape(16.dp), // Angoli arrotondati
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = weather,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(text = nameC, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 20.dp))
            Text(
                text = "${temperature?.toString() ?: "--"} °C",
                modifier = Modifier.align(Alignment.CenterVertically),
                fontWeight = FontWeight.Medium,
            )

        }
    }
}