package com.example.skybaseatmos.ui.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skybaseatmos.common.LocationHelper
import com.example.skybaseatmos.domain.usecase.GetWeathersUseCase
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.common.Result

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng


data class MapUIState(
    val items : List<Weather> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val location: LatLng? = null
)

sealed class MapUiEvent{
    data object StartLocation: MapUiEvent()
    data object StopLocation: MapUiEvent()
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getWeathersUseCase: GetWeathersUseCase,
    private val locationHelper: LocationHelper
):ViewModel() {
    var uiState by mutableStateOf(MapUIState())
        private set

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location=result.lastLocation?: return
            uiState=uiState.copy(location=LatLng(location.latitude, location.longitude))
        }
    }
    init{
        load()

    }
    fun onEvent(event: MapUiEvent){
        when(event){
            is MapUiEvent.StartLocation -> locationHelper.start(locationCallback)
            is MapUiEvent.StopLocation -> locationHelper.stop(locationCallback)
        }
    }
    private fun load(){
        //Corrutine
        viewModelScope.launch {
            getWeathersUseCase(null).collect {
                uiState=when (it){
                    is Result.Loading -> uiState.copy(isLoading = true)
                    is Result.Success -> uiState.copy(items = it.data, isLoading = false)
                    is Result.Error -> uiState.copy(error = it.message, isLoading = false)
                }
            }
        }
    }
}
