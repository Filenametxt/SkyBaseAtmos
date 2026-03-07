package com.example.skybaseatmos.ui.map

import android.Manifest
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.ui.PermissionGate
import com.example.skybaseatmos.ui.forecast.weatherIcon

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState


@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ScreenMap(
    viewModel: MapViewModel = hiltViewModel(),
    onItemClick: (Weather) -> Unit = {}
) {
    val uiState = viewModel.uiState
    val permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
    ) {

        uiState.items.forEach { weather ->

            Marker(
                state = rememberMarkerState(position = LatLng(weather.lat, weather.lon)),
                title = weather.nomeC,
                snippet = weather.weather,
                onInfoWindowClick = {
                    onItemClick(weather)
                },
            )

        }

        if (permissionState.allPermissionsGranted) {
            uiState.location?.let {
                Marker(
                    state = rememberMarkerState(position = it),
                    title = "Localizzazione attuale",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            }
        }
    }

    PermissionGate(permissions = permissions) {
        val localLifeCycle = LocalLifecycleOwner.current
        DisposableEffect(localLifeCycle.lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> viewModel.onEvent(MapUiEvent.StartLocation)
                    Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(MapUiEvent.StopLocation)
                    else -> {}
                }
            }
            localLifeCycle.lifecycle.addObserver(observer)
            onDispose {
                localLifeCycle.lifecycle.removeObserver(observer)
            }
        }
    }
}
