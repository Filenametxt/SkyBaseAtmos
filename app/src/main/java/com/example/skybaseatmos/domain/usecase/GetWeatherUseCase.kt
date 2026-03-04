package com.example.skybaseatmos.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import com.example.skybaseatmos.data.local.WeatherCache
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.repositories.LocalRepository

import com.example.skybaseatmos.common.Result
/*
*  si occupa di gestire il recupero dei dati meteo salvati localmente
* * nel database (tramite LocalRepository),
* include anche una logica di aggiornamento della cache.
*/
class GetWeathersUseCase @Inject constructor(

    private val localRepository: LocalRepository,
    private val weatherCache: WeatherCache
)
{
    operator fun invoke(weather: Weather?): Flow<Result<List<Weather>>> = flow {
        emit(Result.Loading("Loading..."))

        runCatching {
            // Aggiorna la cache (lancia eccezione se fallisce)
            try {
                weatherCache.validCache()
            } catch (e: Exception) {
                // Ignoriamo l'errore di rete qui per permettere la lettura dal DB
                Log.e("GetWeathersUseCase","Impossibile recuperare i dati meteo. Controlla la connessione")
            }
        if(weather!=null) {
            if (localRepository.check(weather)) {
                localRepository.remove(weather)
                localRepository.save(weather)
            }
        }
            val localData = localRepository.getAll()
            emit(Result.Success(localData))
        }.onFailure {
            emit(Result.Error(it.message ?: "Unknown error"))
        }
    }

}
