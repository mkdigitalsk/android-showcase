package com.mk.androidshowcase.data.network

import kotlinx.serialization.json.Json

object NetworkModule {

    const val BASE_URL = "https://kmp-showcase-production.up.railway.app/api/v1/"
    const val TIMEOUT_SECONDS = 30L

    val json: Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}
