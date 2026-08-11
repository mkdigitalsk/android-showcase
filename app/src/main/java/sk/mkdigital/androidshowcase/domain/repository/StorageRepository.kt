package sk.mkdigital.androidshowcase.domain.repository

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.domain.model.StorageData

interface StorageRepository : ClearableCache {
    val storageData: Flow<StorageData>

    suspend fun loadInitialData()
    suspend fun setSessionCounter(value: Int)
    suspend fun setPersistentCounter(value: Int)
    suspend fun clearCounters()
}
