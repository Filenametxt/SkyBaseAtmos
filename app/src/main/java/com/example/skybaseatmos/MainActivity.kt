package com.example.skybaseatmos

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.skybaseatmos.data.local.WeatherCache
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.repositories.LocalRepository
import com.example.skybaseatmos.ui.PermissionGate
import com.example.skybaseatmos.ui.forecast.ForecastDetail
import com.example.skybaseatmos.ui.favList.ScreenFav
import com.example.skybaseatmos.ui.theme.SkyBaseAtmosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants
import javax.inject.Inject

data object LocalInfoScreen
data object FavScreen
data object MapScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var weatherCache: WeatherCache
    @Inject lateinit var localRepository: LocalRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkyBaseAtmosTheme {
                PermissionGate(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    onPermissionAllowed = {
                        UI(weatherCache, localRepository)
                    }
                )
            }
        }
    }
}

@Composable
fun UI(weatherCache: WeatherCache, localRepository: LocalRepository) {
    val backstack = remember { mutableStateListOf<Any>(LocalInfoScreen)}
    var weatherData by remember { mutableStateOf<Weather?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Stato dei preferiti per la località corrente
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            val data = weatherCache.getForecast()
            weatherData = data
            // Verifica se è già nei preferiti
            data?.let {
                isFavorite = localRepository.check(it)
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            SkyBottom(backstack)
        }
    ) {innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backstack,
            onBack = { backstack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<LocalInfoScreen>{
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when {
                            isLoading -> CircularProgressIndicator()
                            error != null -> Text(text = "Errore: $error")
                            weatherData != null -> ForecastDetail(weatherData!!)
                            else -> Text("Dati non disponibili")
                        }
                        
                        if (weatherData != null) {
                            isFavorite=FavButton(localRepository,isFavorite,scope,weatherData!!)
                        }
                    }
                }
                entry<FavScreen>{
                    ScreenFav()
                }
                entry<MapScreen> { }
            }
        )
    }
}

@Composable
fun SkyBottom(backstack: SnapshotStateList<Any>){
    NavigationBar {
        NavigationBarItem(
            selected = backstack.last() is LocalInfoScreen,
            onClick = {
                if (backstack.lastOrNull() !is LocalInfoScreen)
                    backstack.add(LocalInfoScreen)
            },
            icon ={ Icon(imageVector=Icons.Default.Home,contentDescription="Info")},
            label = {Text("Info")}
        )
        NavigationBarItem(
            selected = backstack.last() is FavScreen,
            onClick = {
                if (backstack.lastOrNull() !is FavScreen)
                    backstack.add(FavScreen)
            },
            icon ={ Icon(imageVector=Icons.Default.Star,contentDescription="Favourites")},
            label = {Text("Favourites")}
        )
        NavigationBarItem(
            selected = backstack.last() is MapScreen,
            onClick = {
                if (backstack.lastOrNull() !is MapScreen)
                    backstack.add(MapScreen)
            },
            icon ={ Icon(imageVector=Icons.Default.LocationOn,contentDescription="Map")},
            label = {Text("Map")}
        )
    }
}
@Composable

fun BoxScope.FavButton(localRepository: LocalRepository, isFavorite: Boolean, scope: CoroutineScope, weatherData: Weather): Boolean{
    var fav=isFavorite
    FloatingActionButton(
        onClick = {
            scope.launch {
                if (isFavorite) {
                    localRepository.remove(weatherData)
                } else {
                    localRepository.save(weatherData)
                }
                fav = !fav
            }
        },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 50.dp, end = 30.dp),
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = "Preferiti"
        )
    }
    return fav
}
