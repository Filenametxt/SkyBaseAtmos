package com.example.skybaseatmos.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import com.example.skybaseatmos.data.local.WeatherCache
import com.example.skybaseatmos.domain.model.Weather
import com.example.skybaseatmos.domain.repositories.LocalRepository

import com.example.skybaseatmos.common.Result
class GetWeathersUseCase @Inject constructor(

    private val localRepository: LocalRepository,
    private val weatherCache: WeatherCache
)
{
    operator fun invoke(weather: Weather): Flow<Result<List<Weather>>> = flow {
        emit(Result.Loading("Loading..."))

        runCatching {
            // Aggiorna la cache (lancia eccezione se fallisce)
            weatherCache.validCache()

            if (localRepository.check(weather)){
                localRepository.remove(weather)
                localRepository.save(weather)
            }
            val localData = localRepository.getAll()
            emit(Result.Success(localData))
        }.onFailure {
            emit(Result.Error(it.message ?: "Unknown error"))
        }
    }
}
