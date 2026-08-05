package sk.mkdigital.androidshowcase.domain.useCase.notes

import sk.mkdigital.androidshowcase.domain.model.Note
import sk.mkdigital.androidshowcase.domain.repository.NoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) : UseCase<Note, Unit>() {
    override suspend fun run(params: Note) = noteRepository.update(params)
}
