package sk.mkdigital.androidshowcase.domain.useCase.note

import sk.mkdigital.androidshowcase.domain.model.RemoteNote
import sk.mkdigital.androidshowcase.domain.repository.RemoteNoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class GetRemoteNotesUseCase @Inject constructor(
    private val repository: RemoteNoteRepository
) : UseCase<None, List<RemoteNote>>() {
    override suspend fun run(params: None): List<RemoteNote> = repository.getNotes()
}
