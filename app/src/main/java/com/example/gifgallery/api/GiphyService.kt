package com.example.gifgallery.api

import com.example.gifgallery.api.models.dto.GifsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {

    @GET("/v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("limit") limit: Int = GiphyApi.PAGE_SIZE,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): Response<GifsDto>

    @GET("/v1/gifs/search")
    suspend fun getGifsByQuery(
        @Query("q") query: String,
        @Query("limit") limit: Int = GiphyApi.PAGE_SIZE,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): Response<GifsDto>
}