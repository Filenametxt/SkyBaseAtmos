package com.example.skybaseatmos.modules

import android.content.Context
import androidx.room.Room
import com.example.skybaseatmos.data.local.AppDatabase
import com.example.skybaseatmos.data.local.RoomLocalRepository
import com.example.skybaseatmos.data.local.WeatherDAO
import com.example.skybaseatmos.domain.repositories.LocalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context)= Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()

    @Provides
    fun provideWeatherDao(appDatabase: AppDatabase): WeatherDAO {
        return appDatabase.weatherDao()
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        roomLocalRepository: RoomLocalRepository
    ): LocalRepository

}