package sk.mkdigital.androidshowcase.domain.useCase.notes

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.domain.model.Note
import sk.mkdigital.androidshowcase.domain.model.NoteSortOption
import sk.mkdigital.androidshowcase.domain.repository.NoteRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.FlowUseCase
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) : FlowUseCase<SearchNotesUseCase.Params, List<Note>>() {

    override fun run(params: Params): Flow<List<Note>> {
        return if (params.query.isBlank()) {
            repository.observeAll(params.sortOption)
        } else {
            repository.search(params.query, params.sortOption)
        }
    }

    data class Params(
        val query: String = "",
        val sortOption: NoteSortOption = NoteSortOption.DATE_DESC,
    )
}
