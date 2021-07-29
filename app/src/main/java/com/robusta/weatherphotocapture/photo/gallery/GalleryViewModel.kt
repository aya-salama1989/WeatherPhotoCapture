package com.robusta.weatherphotocapture.photo.gallery

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val applicationContext by lazy { application.applicationContext }

    private var _imagesList: MutableLiveData<List<Uri>> = MutableLiveData()

    val imagesList: LiveData<List<Uri>> get() = _imagesList

    init {
        getImagesList()
    }


    private fun getImagesList() {
        val projection =
            arrayOf(MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?"
        val selectionArgs = arrayOf("WeatherPhotoCapture")

        applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            Log.e(this.javaClass.simpleName, cursor.count.toString())
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val data = ArrayList<Uri>()

            var i = 0
            while (cursor.moveToNext() && i< cursor.count) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                data.add(contentUri)
                i++
            }
            cursor.close()
            _imagesList.value = data
        }


    }
}