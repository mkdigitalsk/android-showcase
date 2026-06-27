package com.mk.androidshowcase.data.network

import com.mk.androidshowcase.data.local.preferences.PersistentPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val preferences: PersistentPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.header(HEADER_AUTHORIZATION) != null) {
            return chain.proceed(request)
        }
        val token = runBlocking { preferences.getToken() } ?: return chain.proceed(request)
        val authorized = request.newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer $token")
            .build()
        return chain.proceed(authorized)
    }

    private companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
    }
}
