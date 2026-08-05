package sk.mkdigital.androidshowcase.domain.useCase.storage

import sk.mkdigital.androidshowcase.domain.repository.StorageRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class LoadStorageDataUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) : UseCase<None, Unit>() {
    override suspend fun run(params: None) = storageRepository.loadInitialData()
}
