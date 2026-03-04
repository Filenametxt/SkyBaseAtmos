package com.example.skybaseatmos.modules

import com.example.skybaseatmos.data.remote.RetrofitRemoteRepository
import com.example.skybaseatmos.data.remote.service.EndpointService
import com.example.skybaseatmos.domain.repositories.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun provideClient() = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun service(client: Retrofit) = client.create(EndpointService::class.java)

}
@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRepositoryModule{
    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        retrofitRemoteRepository: RetrofitRemoteRepository
    ): RemoteRepository

}