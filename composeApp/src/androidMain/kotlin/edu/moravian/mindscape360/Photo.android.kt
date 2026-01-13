package edu.moravian.mindscape360

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

internal actual fun loadImage(imageData: ByteArray): ImageBitmap {
    return BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap()
}
