package com.robusta.weatherphotocapture.extensions

import android.content.Context
import com.robusta.weatherphotocapture.R
import java.io.File


fun getOutputDirectory(context:Context): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) {
        mediaDir
    } else {
        context.filesDir
    }
}