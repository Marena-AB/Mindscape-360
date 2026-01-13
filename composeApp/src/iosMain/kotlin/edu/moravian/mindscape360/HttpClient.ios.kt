package edu.moravian.mindscape360

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://mindscape-360.s3.us-east-1.amazonaws.com"

actual fun createHttpClient(): HttpClient = HttpClient(Darwin) {
    expectSuccess = true

    defaultRequest { url(BASE_URL) }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        })
    }

    install(HttpTimeout) {
        connectTimeoutMillis = 2_000
        socketTimeoutMillis = 2_000
        requestTimeoutMillis = 60_000
    }

    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.HEADERS
    }
}
