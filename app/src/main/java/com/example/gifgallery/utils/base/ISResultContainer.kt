package com.example.gifgallery.utils.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gifgallery.utils.SResult

interface ISResultContainer {
    val resultLiveData: LiveData<*>?
    val supportLiveData: MutableLiveData<SResult<*>>?
    val toastLiveData: MutableLiveData<SResult.Toast>
    val navLiveData: MutableLiveData<SResult.NavResult>
}