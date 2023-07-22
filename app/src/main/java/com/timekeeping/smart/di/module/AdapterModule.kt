package com.timekeeping.smart.di.module

import android.content.Context
import com.timekeeping.smart.base.adapter.ProgressAdapter
import com.timekeeping.smart.ui.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {
    @Provides
    @Singleton
    fun provideProgressExaminationAdapter(context: Context): ProgressAdapter {
        return ProgressAdapter(context)
    }

}