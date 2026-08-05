package sk.mkdigital.androidshowcase.domain.useCase.biometric

import sk.mkdigital.androidshowcase.data.biometric.BiometricResult
import sk.mkdigital.androidshowcase.domain.repository.BiometricRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class AuthenticateWithBiometricUseCase @Inject constructor(
    private val biometricRepository: BiometricRepository
) : UseCase<None, BiometricResult>() {
    override suspend fun run(params: None): BiometricResult = biometricRepository.authenticate()
}
