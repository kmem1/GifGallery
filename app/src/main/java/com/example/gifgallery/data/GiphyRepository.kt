package com.example.gifgallery.data

import androidx.paging.PagingData
import com.example.gifgallery.domain.models.GifModel
import kotlinx.coroutines.flow.Flow

interface GiphyRepository {

    fun getTrendingGifsFlow(): Flow<PagingData<GifModel>>
    fun getGifsByQueryFlow(query: String): Flow<PagingData<GifModel>>
}