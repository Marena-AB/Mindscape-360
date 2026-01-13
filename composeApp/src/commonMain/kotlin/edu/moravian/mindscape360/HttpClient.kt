package edu.moravian.mindscape360

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient

val client: HttpClient by lazy { createHttpClient() }
