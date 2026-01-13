package edu.moravian.mindscape360

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

internal actual fun loadImage(imageData: ByteArray): ImageBitmap {
    return Image.makeFromEncoded(imageData).toComposeImageBitmap()
}