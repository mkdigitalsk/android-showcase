package sk.mkdigital.androidshowcase.domain.useCase.note

import sk.mkdigital.androidshowcase.domain.model.RemoteNote
import sk.mkdigital.androidshowcase.domain.repository.RemoteNoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class CreateRemoteNoteUseCase @Inject constructor(
    private val repository: RemoteNoteRepository
) : UseCase<CreateRemoteNoteUseCase.Params, RemoteNote>() {

    data class Params(val title: String, val content: String)

    override suspend fun run(params: Params): RemoteNote =
        repository.createNote(params.title, params.content)
}
