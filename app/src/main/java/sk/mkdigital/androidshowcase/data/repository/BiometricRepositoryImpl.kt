package sk.mkdigital.androidshowcase.data.repository

import sk.mkdigital.androidshowcase.data.biometric.BiometricClient
import sk.mkdigital.androidshowcase.data.biometric.BiometricResult
import sk.mkdigital.androidshowcase.domain.repository.BiometricRepository
import javax.inject.Inject

class BiometricRepositoryImpl @Inject constructor(
    private val biometricClient: BiometricClient,
) : BiometricRepository {

    override fun enabled(): Boolean = biometricClient.enabled()

    override suspend fun authenticate(): BiometricResult = biometricClient.authenticate()
}
