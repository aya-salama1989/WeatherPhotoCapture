package com.robusta.weatherphotocapture.extensions

import android.content.Context
import android.graphics.*
import android.text.StaticLayout

import android.text.TextPaint

import android.text.Layout
import androidx.camera.core.ImageProxy

import android.os.Build
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import com.robusta.weatherphotocapture.photo.capture.CapturePhotoViewModel


fun ImageProxy.convertToBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}


fun Bitmap.drawMultilineTextToBitmap(text1: String, text: String): Bitmap {

    var myStaticLayout: StaticLayout

    var newBitmap = this.copy(this.config, true)
    val canvas = Canvas(newBitmap)

    // new antialiased Paint
    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    // text color - #3D3D3D
    paint.color = Color.rgb(128, 0, 0)
    // text size in pixels
    paint.textSize = (14 * 1).toFloat()
    // text shadow
    paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)

    // set text width to canvas width minus 16dp padding
    val textWidth = canvas.width - (16 * 1)

    // init StaticLayout for text
    myStaticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val builder =  StaticLayout.Builder.obtain(text + text1 ,0, text.length, paint, textWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(1.0f, 1.0f)
            .setIncludePad(false)
            .setMaxLines(5)
        builder.build()
    } else {
        StaticLayout(
            text + text1, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false
        )
    }

    canvas.drawText("This is that", 100f, 100f, paint);
    // get height of multiline text
    val textHeight = myStaticLayout.height

    // get position of text's top left corner
    val x = (newBitmap.width.minus(textWidth) / 2).toFloat()
    val y = ((newBitmap.height - textHeight) / 2).toFloat()

    // draw text to the Canvas center
    canvas.save()
    canvas.translate(x, y)
    myStaticLayout.draw(canvas)
    canvas.restore()
    return newBitmap
}


fun drawTextToBitmap(
    gContext: Context,
    gText: String,
    bitmap: Bitmap
): Bitmap {
    var bitmapConfig = bitmap.config
    // set default bitmap config if none
    if (bitmapConfig == null) {
        bitmapConfig = Bitmap.Config.ARGB_8888
    }
    // resource bitmaps are imutable,
    // so we need to convert it to mutable one
    var bitmap2 = bitmap.copy(bitmapConfig, true)
    val canvas = Canvas(bitmap2)
    // new antialised Paint
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // text color - #3D3D3D
    paint.color = Color.rgb(61, 61, 61)
    // text size in pixels
    paint.textSize = 14f
    // text shadow
    paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)

    // draw text to the Canvas center
    val bounds = Rect()
    paint.getTextBounds(gText, 0, gText.length, bounds)
    val x = (bitmap2.width - bounds.width()) / 2
    val y = (bitmap2.height + bounds.height()) / 2
    canvas.drawText(gText, x.toFloat(), y.toFloat(), paint)
    return bitmap2
}