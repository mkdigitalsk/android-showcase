package sk.mkdigital.androidshowcase.domain.useCase.storage

import sk.mkdigital.androidshowcase.domain.repository.StorageRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class SetSessionCounterUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) : UseCase<Int, Unit>() {
    override suspend fun run(params: Int) = storageRepository.setSessionCounter(params)
}
