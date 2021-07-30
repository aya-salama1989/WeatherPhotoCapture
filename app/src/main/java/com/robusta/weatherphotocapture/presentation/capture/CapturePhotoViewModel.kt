package com.robusta.weatherphotocapture.presentation.capture

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.robusta.weatherphotocapture.extensions.convertToBitMap


class CapturePhotoViewModel(application: Application) : AndroidViewModel(application) {

    //navigate when capture success
    private var _navigateToPhotoView: MutableLiveData<Bitmap> = MutableLiveData()
    val navigateToPhotoView: LiveData<Bitmap>
        get() = _navigateToPhotoView

    private val context by lazy { application.applicationContext }

    fun capturePhoto(imageCapture: ImageCapture) {
        imageCapture.takePicture(ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    _navigateToPhotoView.value = image.convertToBitMap()
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(
                        "CapturePhotoViewModel",
                        "Photo capture failed: ${exception.message}",
                        exception
                    )
                }
            }
        )
    }
}