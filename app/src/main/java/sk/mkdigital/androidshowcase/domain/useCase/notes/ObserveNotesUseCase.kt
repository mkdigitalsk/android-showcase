package sk.mkdigital.androidshowcase.domain.useCase.notes

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.domain.model.Note
import sk.mkdigital.androidshowcase.domain.repository.NoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.FlowUseCase
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import javax.inject.Inject

class ObserveNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) : FlowUseCase<None, List<Note>>() {
    override fun run(params: None): Flow<List<Note>> = noteRepository.observeAll()
}
