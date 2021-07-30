package com.robusta.weatherphotocapture.presentation.view

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.robusta.weatherphotocapture.presentation.datasource.PhotoDataSource
import com.robusta.weatherphotocapture.presentation.datasource.WeatherDataSource
import com.robusta.weatherphotocapture.networking.Main
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PhotoPreviewVM(application: Application) : AndroidViewModel(application) {

    private val weatherDataSource = WeatherDataSource()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _temperature: MutableLiveData<Main> = MutableLiveData()
    val temperature: LiveData<Main>
        get() = _temperature

    private var _navigateToGallery: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToGallery: LiveData<Boolean>
        get() = _navigateToGallery

    fun getWeatherData(cityName: String) {
        coroutineScope.launch {
            val data = weatherDataSource.getWeatherData(cityName)
            try {
                _temperature.value = data.main
            } catch (e: Exception) {
                Log.e("error", data.toString())
            }
        }

    }

    fun saveImage(imageBitMap: Bitmap) {
        val photoDataSource = PhotoDataSource()
        photoDataSource.saveImage(imageBitMap, getApplication())?.subscribe({
            _navigateToGallery.value = true
        }, {
            //show error and navigate back to take a photo
            //Doesn't determine saving image error
            //TODO: find a solution for error!
            _navigateToGallery.value = false
        })
    }
}