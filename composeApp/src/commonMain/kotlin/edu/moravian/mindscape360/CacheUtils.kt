package edu.moravian.mindscape360

import kotlinx.datetime.Clock
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.time.Duration

/**
 * Check if a cached file has expired based on its last modified time
 *
 * @param path The file path to check
 * @param ttl Time-to-live duration (e.g., 30.days)
 * @return true if file is expired or age cannot be determined, false if still valid
 */
fun isCacheExpired(path: String, ttl: Duration): Boolean {
    val metadata = FileSystem.SYSTEM.metadataOrNull(path.toPath()) ?: return true
    val lastModified = metadata.lastModifiedAtMillis ?: return false // Can't determine age, assume valid

    val now = Clock.System.now().toEpochMilliseconds()
    val age = now - lastModified
    val isExpired = age > ttl.inWholeMilliseconds

    if (isExpired) {
        val ageDays = age / (1000 * 60 * 60 * 24)
        println("Cache expired (age: $ageDays days)")
    }

    return isExpired
}

/**
 * Get the age of a cached file in days
 *
 * @param path The file path to check
 * @return Age in days, or null if cannot be determined
 */
fun getCacheAge(path: Path): Long? {
    val metadata = FileSystem.SYSTEM.metadataOrNull(path) ?: return null
    val lastModified = metadata.lastModifiedAtMillis ?: return null

    val now = Clock.System.now().toEpochMilliseconds()
    val age = now - lastModified
    return age / (1000 * 60 * 60 * 24)
}

