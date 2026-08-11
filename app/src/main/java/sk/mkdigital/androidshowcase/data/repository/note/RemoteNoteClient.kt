package sk.mkdigital.androidshowcase.data.repository.note

import sk.mkdigital.androidshowcase.data.network.NetworkModule
import retrofit2.HttpException
import sk.mkdigital.androidshowcase.data.dto.note.CreateNoteRequestDTO
import sk.mkdigital.androidshowcase.data.dto.note.NoteResponseDTO
import sk.mkdigital.androidshowcase.data.dto.note.UpdateNoteRequestDTO
import sk.mkdigital.androidshowcase.data.network.NoteApi
import sk.mkdigital.androidshowcase.data.network.handleApiCall
import sk.mkdigital.androidshowcase.domain.exceptions.NoteConflictException
import javax.inject.Inject

private const val PRECONDITION_FAILED = 412

interface RemoteNoteClient {
    suspend fun fetchNotes(): List<NoteResponseDTO>
    suspend fun createNote(request: CreateNoteRequestDTO): NoteResponseDTO
    suspend fun updateNote(id: Long, request: UpdateNoteRequestDTO, etag: String): NoteResponseDTO
    suspend fun deleteNote(id: Long)
}

class RemoteNoteClientImpl @Inject constructor(
    private val noteApi: NoteApi,
) : RemoteNoteClient {

    override suspend fun fetchNotes(): List<NoteResponseDTO> = handleApiCall {
        noteApi.fetchNotes()
    }

    override suspend fun createNote(request: CreateNoteRequestDTO): NoteResponseDTO = handleApiCall {
        noteApi.createNote(request)
    }

    override suspend fun updateNote(id: Long, request: UpdateNoteRequestDTO, etag: String): NoteResponseDTO =
        handleApiCall {
            try {
                noteApi.updateNote(id, etag, request)
            } catch (e: HttpException) {
                // handleApiCall turns every 4xx into one generic exception, which would discard the row a
                // 412 carries — the only thing that lets someone choose between the two versions.
                if (e.code() == PRECONDITION_FAILED) {
                    val body = e.response()?.errorBody()?.string().orEmpty()
                    val current = NetworkModule.json.decodeFromString<NoteResponseDTO>(body)
                    throw NoteConflictException(current.transform(), e)
                }
                throw e
            }
        }

    override suspend fun deleteNote(id: Long) = handleApiCall {
        noteApi.deleteNote(id)
    }
}
