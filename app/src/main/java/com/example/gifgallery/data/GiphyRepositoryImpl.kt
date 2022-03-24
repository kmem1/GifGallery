package com.example.gifgallery.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.gifgallery.api.GiphyApi
import com.example.gifgallery.api.GiphyService
import com.example.gifgallery.api.paging.GifsByQueryPageSource
import com.example.gifgallery.api.paging.TrendingGifsPageSource
import com.example.gifgallery.domain.models.GifModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GiphyRepositoryImpl @Inject constructor(
    private val giphyService: GiphyService
) : GiphyRepository {

    override fun getTrendingGifsFlow(): Flow<PagingData<GifModel>> {
        return getPager(TrendingGifsPageSource(giphyService)).flow
    }

    override fun getGifsByQueryFlow(query: String): Flow<PagingData<GifModel>> {
        return getPager(GifsByQueryPageSource(giphyService, query)).flow
    }
}

private fun <T : Any, V : Any> getPager(pagingSource: PagingSource<T, V>): Pager<T, V> {
    return Pager(
        config = PagingConfig(
            pageSize = GiphyApi.PAGE_SIZE,
            initialLoadSize = GiphyApi.INITIAL_LOAD_SIZE
        ),
        pagingSourceFactory = { pagingSource }
    )
}