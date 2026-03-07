package com.example.skybaseatmos

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.skybaseatmos.ui.PermissionGate
import com.example.skybaseatmos.ui.forecast.ForecastScreen
import com.example.skybaseatmos.ui.favList.ScreenFav
import com.example.skybaseatmos.ui.theme.SkyBaseAtmosTheme
import dagger.hilt.android.AndroidEntryPoint

data object LocalInfoScreen
data object FavScreen
data object MapScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                        UI()
                    }
                )
            }
        }
    }
}

@Composable
fun UI() {
    val backstack = remember { mutableStateListOf<Any>(LocalInfoScreen)}

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
                    ForecastScreen()
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
