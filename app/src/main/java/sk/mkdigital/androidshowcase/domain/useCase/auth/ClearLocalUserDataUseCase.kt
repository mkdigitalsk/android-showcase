package sk.mkdigital.androidshowcase.domain.useCase.auth

import sk.mkdigital.androidshowcase.domain.repository.AuthRepository
import sk.mkdigital.androidshowcase.domain.repository.NoteRepository
import sk.mkdigital.androidshowcase.domain.repository.NotificationRepository
import sk.mkdigital.androidshowcase.domain.repository.StorageRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject
import sk.mkdigital.androidshowcase.util.suspendRunCatching

/**
 * Each store clears independently: a thrown one must not skip the token clear below it, or the person
 * stays signed in on a device that has already half-erased itself.
 */
class ClearLocalUserDataUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
    private val storageRepository: StorageRepository,
    private val notificationRepository: NotificationRepository,
) : UseCase<None, Unit>() {

    override suspend fun run(params: None) {
        suspendRunCatching { noteRepository.deleteAll() }
        suspendRunCatching { storageRepository.clearCounters() }
        suspendRunCatching { notificationRepository.clearToken() }
        authRepository.signOut()
    }
}
