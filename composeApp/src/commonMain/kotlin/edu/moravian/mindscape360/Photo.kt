package edu.moravian.mindscape360

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.time.Duration.Companion.days

private val imageStore = mutableMapOf<String, ImageBitmap>()
private val IMAGE_TTL = 30.days // Images expire after 30 days

private suspend fun getImage(url: String, refresh: Boolean = false): ImageBitmap? {
    if (refresh || url !in imageStore) {
        val path = filePathTo(url.hashCode().toString()).toPath()
        val metadata = FileSystem.SYSTEM.metadataOrNull(path)

        val needsDownload = when {
            refresh -> true
            metadata == null -> true
            isCacheExpired(path.toString(), IMAGE_TTL) -> {
                println("Image cache expired for: $url")
                true
            }
            else -> false
        }

        if (needsDownload) {
            val raw = client.get(url).body() as ByteArray
            FileSystem.SYSTEM.write(path) { write(raw) }
            imageStore[url] = loadImage(raw)
        } else {
            imageStore[url] = loadImage(FileSystem.SYSTEM.read(path) { readByteArray() })
        }
    }
    return imageStore[url]
}

internal expect fun loadImage(imageData: ByteArray): ImageBitmap

@Composable
fun Photo(url: String, description: String, scope: CoroutineScope, modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null
                image = getImage(url, refresh = false)
                if (image == null) { errorMessage = "Failed to load image" }
            } catch (e: Exception) {
                errorMessage = "Error loading image: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = modifier.background(Color.LightGray),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = Color(0xFF4F7942)
            )
        }
    } else if (errorMessage != null) {
        Box(
            modifier = modifier.background(Color.LightGray),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = errorMessage ?: "Unknown error", color = Color.Red)
        }
    } else if (image != null) {
        Image(
            image!!,
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        println("Nothing to display?!?")
    }
}

