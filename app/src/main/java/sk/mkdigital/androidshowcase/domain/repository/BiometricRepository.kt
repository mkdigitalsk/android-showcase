package sk.mkdigital.androidshowcase.domain.repository

import sk.mkdigital.androidshowcase.data.biometric.BiometricResult

interface BiometricRepository {
    fun enabled(): Boolean
    suspend fun authenticate(): BiometricResult
}
