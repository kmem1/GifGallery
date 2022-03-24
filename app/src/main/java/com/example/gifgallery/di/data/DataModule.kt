package com.example.gifgallery.di.data

import com.example.gifgallery.api.GiphyApi
import com.example.gifgallery.api.GiphyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DataBinds::class])
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideGiphyService(): GiphyService = GiphyApi.service
}