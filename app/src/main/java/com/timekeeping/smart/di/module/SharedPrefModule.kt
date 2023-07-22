package com.timekeeping.smart.di.module

import com.timekeeping.smart.share_preference.HSBASharePref
import com.timekeeping.smart.share_preference.HSBASharePrefImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPrefModule {
    @Provides
    @Singleton
    fun provideSharePref(sharedPref: HSBASharePrefImpl): HSBASharePref {
        return sharedPref
    }
}