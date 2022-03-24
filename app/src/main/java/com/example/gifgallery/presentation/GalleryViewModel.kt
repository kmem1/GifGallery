package com.example.gifgallery.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gifgallery.data.GiphyRepository
import com.example.gifgallery.domain.models.GifModel
import com.example.gifgallery.utils.base.BaseBindingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: GiphyRepository
) : BaseBindingViewModel() {

    private val _gifsFlow = MutableStateFlow<Flow<PagingData<GifModel>>>(emptyFlow())
    val gifsFlow = _gifsFlow.asStateFlow()

    private var searchingJob: Job? = null

    fun searchGifs(query: String) {
        searchingJob?.cancel()

        searchingJob = viewModelScope.launch {
            // Delay to avoid too much requests
            delay(SEARCHING_DELAY)
            if (query.isEmpty()) {
                setTrendingGifs()
            } else {
                _gifsFlow.value = repository.getGifsByQueryFlow(query).cachedIn(viewModelScope)
            }
        }
    }

    fun setTrendingGifs() {
        _gifsFlow.value = repository.getTrendingGifsFlow().cachedIn(viewModelScope)
    }

    companion object {
        private const val SEARCHING_DELAY = 500L
    }
}