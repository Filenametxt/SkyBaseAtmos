package com.example.skybaseatmos.ui.forecast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skybaseatmos.data.local.WeatherCache
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.repositories.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForecastUIState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false
)

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherCache: WeatherCache,
    private val localRepository: LocalRepository
) : ViewModel() {

    var uiState by mutableStateOf(ForecastUIState())
        private set

    init {
        load()
    }
    fun refresh(){
        load()
    }

    fun load() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val data = weatherCache.getWeatherUpdated()
                val isFav = if (data != null) localRepository.check(data) else false
                uiState = uiState.copy(
                    weather = data,
                    isLoading = false,
                    isFavorite = isFav
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Errore sconosciuto"
                )
            }
        }
    }

    fun toggleFavorite() {
        val currentWeather = uiState.weather ?: return
        viewModelScope.launch {
            if (uiState.isFavorite) {
                localRepository.remove(currentWeather)
            } else {
                localRepository.save(currentWeather)
            }
            uiState = uiState.copy(isFavorite = !uiState.isFavorite)
        }
    }
}

