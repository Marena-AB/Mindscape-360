package edu.moravian.mindscape360

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import okio.Path.Companion.toPath
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

expect fun filePathTo(fileName: String): String


/**
 * Wrapper around KStore data storage but additionally expires values and
 * re-fetches them automatically. Exposes all of the same functions as KStore.
 */
class CacheStore<T : @Serializable Any>(
    private val store: KStore<WithExpiration<T>>,
    private val fetch: suspend () -> T,
    private val ttl: Duration = 1.days,
) {
    @Serializable
    data class WithExpiration<T : @Serializable Any>(val value: T, val expireAt: Instant)

    private fun expires(value: T?) =
        value?.let { WithExpiration(it, Clock.System.now() + ttl) }

    private suspend fun fetchOrNull(): T? =
        try { fetch().also { store.set(expires(it)) } } catch (_: Exception) { null }

    suspend fun get(refresh: Boolean = false): T? {
        val value = try { store.get() } catch (_: Exception) { store.delete(); null }
        return when {
            value === null -> fetchOrNull()
            refresh || value.expireAt < Clock.System.now() -> fetchOrNull() ?: value.value
            else -> value.value
        }
    }

    suspend fun set(value: T?) = store.set(expires(value))
}

inline fun <reified T : @Serializable Any> cacheStoreOf(
    filename: String,
    noinline fetch: suspend () -> T,
    ttl: Duration = 1.days,
): CacheStore<T> {
    val path = filePathTo(filename).toPath()
    val store: KStore<CacheStore.WithExpiration<T>> = storeOf(file = path)
    return CacheStore(store, fetch, ttl)
}