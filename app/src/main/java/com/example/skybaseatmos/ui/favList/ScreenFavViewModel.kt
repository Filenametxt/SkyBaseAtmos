package com.example.skybaseatmos.ui.favList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.usecase.GetWeathersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.skybaseatmos.common.Result
import com.example.skybaseatmos.data.local.WeatherCache

data class ListUIState(
    val items : List<Weather> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ScreenFavViewModel @Inject constructor(
    private val getWeathersUseCase: GetWeathersUseCase,
    private val weatherCache: WeatherCache
): ViewModel() {
    var uiState by mutableStateOf(ListUIState())
        private set

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            
            val weather: Weather? = try {
                weatherCache.getForecast()
            } catch (e: Exception) {
                null
            }

            getWeathersUseCase(weather).collect { result ->
                uiState = when (result) {
                    is Result.Loading -> uiState.copy(isLoading = true)
                    is Result.Success -> uiState.copy(items = result.data, isLoading = false, error = null)
                    is Result.Error -> uiState.copy(error = result.message, isLoading = false, items = emptyList())
                }
            }
            /*if (weather != null) {
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Impossibile recuperare i dati meteo. Controlla la connessione."
                )
            }*/
        }
    }
}
