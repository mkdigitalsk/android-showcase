package sk.mkdigital.androidshowcase.domain.useCase.biometric

import sk.mkdigital.androidshowcase.domain.repository.BiometricRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class IsBiometricEnabledUseCase @Inject constructor(
    private val biometricRepository: BiometricRepository
) : UseCase<None, Boolean>() {
    override suspend fun run(params: None): Boolean = biometricRepository.enabled()
}
