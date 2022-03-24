package com.example.gifgallery.api.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gifgallery.api.GiphyApi
import com.example.gifgallery.api.GiphyService
import com.example.gifgallery.domain.models.GifModel
import retrofit2.HttpException

class TrendingGifsPageSource(
    private val giphyService: GiphyService
) : PagingSource<Int, GifModel>() {

    override fun getRefreshKey(state: PagingState<Int, GifModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifModel> {
        val page: Int = params.key ?: 1
        val pageSize: Int = params.loadSize.coerceAtMost(GiphyApi.MAX_PAGE_SIZE)

        val offset = when (page) {
            1 -> { 0 }
            2 -> { GiphyApi.INITIAL_LOAD_SIZE.coerceAtMost(GiphyApi.MAX_PAGE_SIZE) }
            else -> { (page - 2) * pageSize + GiphyApi.INITIAL_LOAD_SIZE.coerceAtMost(GiphyApi.MAX_PAGE_SIZE) }
        }

        val response = giphyService.getTrendingGifs(limit = pageSize, offset = offset)

        if (response.isSuccessful) {
            val gifsList = response.body()?.data?.map { GifModel(it.images?.downsized?.url) }
                ?: return LoadResult.Error(HttpException(response))

            val nextKey = if (gifsList.size < pageSize) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            return LoadResult.Page(gifsList, prevKey, nextKey)
        } else {
            return LoadResult.Error(HttpException(response))
        }
    }
}