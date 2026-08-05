package sk.mkdigital.androidshowcase.domain.repository

fun interface ClearableCache {
    suspend fun clear()
}
