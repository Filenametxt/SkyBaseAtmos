package com.example.skybaseatmos.modules

import android.content.Context
import com.example.skybaseatmos.common.LocationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun providerLocation(@ApplicationContext context: Context) = LocationHelper(context)
}
