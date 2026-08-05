package sk.mkdigital.androidshowcase.domain.useCase.storage

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.domain.model.StorageData
import sk.mkdigital.androidshowcase.domain.repository.StorageRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.FlowUseCase
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import javax.inject.Inject

class ObserveStorageDataUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) : FlowUseCase<None, StorageData>() {
    override fun run(params: None): Flow<StorageData> = storageRepository.storageData
}
