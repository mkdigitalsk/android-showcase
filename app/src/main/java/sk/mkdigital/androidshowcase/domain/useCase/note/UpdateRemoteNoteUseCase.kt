package sk.mkdigital.androidshowcase.domain.useCase.note

import sk.mkdigital.androidshowcase.domain.model.RemoteNote
import sk.mkdigital.androidshowcase.domain.repository.RemoteNoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class UpdateRemoteNoteUseCase @Inject constructor(
    private val repository: RemoteNoteRepository
) : UseCase<UpdateRemoteNoteUseCase.Params, RemoteNote>() {

    data class Params(val id: Long, val title: String, val content: String, val etag: String)

    override suspend fun run(params: Params): RemoteNote =
        repository.updateNote(params.id, params.title, params.content, params.etag)
}
