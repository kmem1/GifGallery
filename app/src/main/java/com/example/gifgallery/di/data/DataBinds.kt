package com.example.gifgallery.di.data

import com.example.gifgallery.data.GiphyRepository
import com.example.gifgallery.data.GiphyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataBinds {

    @Binds
    fun bindGiphyRepository(impl: GiphyRepositoryImpl): GiphyRepository
}