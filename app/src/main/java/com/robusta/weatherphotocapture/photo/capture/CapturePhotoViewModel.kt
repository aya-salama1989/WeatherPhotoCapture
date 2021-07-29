package com.robusta.weatherphotocapture.photo.capture

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.robusta.weatherphotocapture.datasource.WeatherDataSource
import com.robusta.weatherphotocapture.extensions.convertToBitmap
import com.robusta.weatherphotocapture.extensions.drawTextToBitmap
import com.robusta.weatherphotocapture.extensions.getOutputDirectory
import com.robusta.weatherphotocapture.networking.Main
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class CapturePhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherDataSource = WeatherDataSource()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    //navigate when capture success
    private var _navigateToGallery: MutableLiveData<Boolean> = MutableLiveData()
    val navigateToGallery: LiveData<Boolean>
        get() = _navigateToGallery


    private var _temperature: MutableLiveData<Main> = MutableLiveData()
    val temperature: LiveData<Main>
        get() = _temperature


    private val context by lazy { application.applicationContext }
    private var outputDirectory: File = getOutputDirectory(context)

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }


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

    fun editPhoto(imageCapture: ImageCapture, cityName: String) {
        imageCapture.takePicture(ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val bitmap = image.convertToBitmap()
                    saveImage(drawTextToBitmap(context, cityName , bitmap))
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }


    fun saveImage(bitmap: Bitmap) {
        val photoFile = File(
            outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".png"
        )
        try {
            val out = FileOutputStream(photoFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _navigateToGallery.value = true
            val savedUri = Uri.fromFile(photoFile)
            Toast.makeText(getApplication(), savedUri.toString(), Toast.LENGTH_SHORT).show()
            Log.d(TAG, savedUri.toString())
        }
    }


}