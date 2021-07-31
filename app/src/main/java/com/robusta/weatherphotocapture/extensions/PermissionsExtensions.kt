package com.robusta.weatherphotocapture.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Kotlin file specified for permissions extension functions
 */

fun Array<String>.allPermissionsGranted(context: Context) = all {
    ContextCompat.checkSelfPermission(
        context, it
    ) == PackageManager.PERMISSION_GRANTED
}