package edu.moravian.mindscape360

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.days


@Serializable
data class VideoData(
    val id: String,
    val title: String,
    val description: String,
    val preview: String,
    @SerialName("approx_min")
    val approxMin: Int,
    val files: List<VideoFile>
)

@Serializable
data class VideoFile(
    val url: String,
    val resolution: String,
    @SerialName("byte_size")
    val byteSize: Double
)

object VideoDataApi {
    private const val JSON_URL = "https://mindscape-360.s3.us-east-1.amazonaws.com/videos-data.json"

    private val store = cacheStoreOf("video_data_cache.json", ::fetch, ttl = 30.days)

    private suspend fun fetch(): List<VideoData> {
        println("=== VideoDataApi.fetch() START ===")
        return try {
            println("Fetching from: $JSON_URL")
            val response = client.get(JSON_URL)
            println("Got HTTP response, parsing body...")
            val data: List<VideoData> = response.body()
            data.forEachIndexed { index, video ->
                println("  Video $index: ${video.title}")
            }
            data
        } catch (e: Exception) {
            println("Exception type: ${e::class.simpleName}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getVideoData(refresh: Boolean = false): List<VideoData> {
        println("=== VideoDataApi.getVideoData(refresh=$refresh) START ===")
        return try {
            println("Calling store.get()...")
            val result = store.get(refresh)

            when {
                result == null -> {
                    emptyList()
                }
                result.isEmpty() -> {
                    emptyList()
                }
                else -> {
                    println("First video: ${result.first().title}")
                    result
                }
            }
        } catch (e: Exception) {
            println("Exception type: ${e::class.simpleName}")
            e.printStackTrace()
            emptyList()
        }
    }
}