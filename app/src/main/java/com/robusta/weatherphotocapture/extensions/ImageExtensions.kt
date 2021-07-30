package com.robusta.weatherphotocapture.extensions


/**
 * Kotlin file specified for image editing extensions
 */


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream


fun ImageProxy.convertToBitMap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    close()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Bitmap.convertToByteArray(): ByteArray {
    val bStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, bStream)
    return bStream.toByteArray()
}


fun ByteArray.convertToBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

fun Bitmap.rotateBitMap(angle: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}


