package com.robusta.weatherphotocapture.presentation.datasource

import android.app.Application
import android.content.ContentUris
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.robusta.weatherphotocapture.extensions.getOutputDirectory
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PhotoDataSource {

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    fun saveImage(bitmap: Bitmap, application: Application):  Observable<Uri>? {
        var outputDirectory: File = getOutputDirectory(application.applicationContext)
        val photoFile = File(
            outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".JPG"
        )
        try {
            val out = FileOutputStream(photoFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            val savedUri = Uri.fromFile(photoFile)
            Log.d("PhotoDataSource", savedUri.toString())
            return  Observable.just(savedUri)
        }
    }

    fun getALlPhotos(application: Application): @NonNull Observable<ArrayList<Uri>>? {
        val data = ArrayList<Uri>()
        val projection =
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?"
        val selectionArgs = arrayOf("WeatherPhotoCapture")

        application.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            Log.e(this.javaClass.simpleName, cursor.count.toString())
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)


            var i = 0
            while (cursor.moveToNext() && i< cursor.count) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                data.add(contentUri)
                i++
            }
            cursor.close()

        }
        return Observable.just(data)
    }
}