package sk.mkdigital.androidshowcase.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import sk.mkdigital.androidshowcase.domain.model.Note
import sk.mkdigital.androidshowcase.domain.model.NoteSortOption
import sk.mkdigital.androidshowcase.domain.repository.NoteRepository

class FakeNoteRepository : NoteRepository {

    var failOnDeleteAll: Boolean = false
    private val notes = MutableStateFlow<List<Note>>(emptyList())

    override fun observeAll(sortOption: NoteSortOption): Flow<List<Note>> = notes

    override fun search(query: String, sortOption: NoteSortOption): Flow<List<Note>> =
        notes.map { stored -> stored.filter { it.title.contains(query) || it.content.contains(query) } }

    override suspend fun getById(id: Long): Note? = notes.value.find { it.id == id }

    override suspend fun insert(note: Note) {
        notes.update { it + note }
    }

    override suspend fun update(note: Note) {
        notes.update { stored -> stored.map { if (it.id == note.id) note else it } }
    }

    override suspend fun delete(id: Long) {
        notes.update { stored -> stored.filterNot { it.id == id } }
    }

    override suspend fun deleteAll() {
        if (failOnDeleteAll) error("the notes table is unreadable")
        notes.value = emptyList()
    }

    override suspend fun count(): Long = notes.value.size.toLong()
}
