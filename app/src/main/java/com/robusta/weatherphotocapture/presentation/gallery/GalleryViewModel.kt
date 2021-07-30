package com.robusta.weatherphotocapture.presentation.gallery

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.robusta.weatherphotocapture.presentation.datasource.PhotoDataSource


class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private var _imagesList: MutableLiveData<List<Uri>> = MutableLiveData()
    val imagesList: LiveData<List<Uri>> get() = _imagesList

    private var _error: MutableLiveData<Throwable> = MutableLiveData()
    val error: LiveData<Throwable> get() = _error

    init {
        getImagesList()
    }


    private fun getImagesList() {
       val photosDataSource = PhotoDataSource()

        photosDataSource.getALlPhotos(getApplication())?.subscribe({
            _imagesList.value = it
        },{
            _error.value = it
        })
    }
}