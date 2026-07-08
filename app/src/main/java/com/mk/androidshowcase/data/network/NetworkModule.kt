package com.mk.androidshowcase.data.network

import com.mk.androidshowcase.BuildConfig
import kotlinx.serialization.json.Json

object NetworkModule {

    val BASE_URL: String = BuildConfig.BASE_URL
    const val TIMEOUT_SECONDS = 30L

    val json: Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}
