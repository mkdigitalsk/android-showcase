package sk.mkdigital.androidshowcase.data.repository.note

import sk.mkdigital.androidshowcase.data.dto.note.CreateNoteRequestDTO
import sk.mkdigital.androidshowcase.data.dto.note.UpdateNoteRequestDTO
import sk.mkdigital.androidshowcase.domain.model.RemoteNote
import sk.mkdigital.androidshowcase.domain.repository.RemoteNoteRepository
import javax.inject.Inject

class RemoteNoteRepositoryImpl @Inject constructor(
    private val client: RemoteNoteClient
) : RemoteNoteRepository {

    override suspend fun getNotes(): List<RemoteNote> =
        client.fetchNotes().map { it.transform() }

    override suspend fun createNote(title: String, content: String): RemoteNote =
        client.createNote(CreateNoteRequestDTO(title, content)).transform()

    override suspend fun updateNote(id: Long, title: String, content: String, etag: String): RemoteNote =
        client.updateNote(id, UpdateNoteRequestDTO(title, content), etag).transform()

    override suspend fun deleteNote(id: Long) = client.deleteNote(id)
}
