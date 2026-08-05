package sk.mkdigital.androidshowcase.domain.useCase.notes

import sk.mkdigital.androidshowcase.domain.repository.NoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class DeleteAllNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) : UseCase<None, Unit>() {
    override suspend fun run(params: None) = noteRepository.deleteAll()
}
