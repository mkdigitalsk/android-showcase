package sk.mkdigital.androidshowcase.domain.useCase.note

import sk.mkdigital.androidshowcase.domain.repository.RemoteNoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class DeleteRemoteNoteUseCase @Inject constructor(
    private val repository: RemoteNoteRepository
) : UseCase<Long, Unit>() {
    override suspend fun run(params: Long) = repository.deleteNote(params)
}
