package sk.mkdigital.androidshowcase.data.network

import sk.mkdigital.androidshowcase.data.dto.note.CreateNoteRequestDTO
import sk.mkdigital.androidshowcase.data.dto.note.NoteResponseDTO
import sk.mkdigital.androidshowcase.data.dto.note.UpdateNoteRequestDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {
    @GET("notes")
    suspend fun fetchNotes(): List<NoteResponseDTO>

    @POST("notes")
    suspend fun createNote(@Body body: CreateNoteRequestDTO): NoteResponseDTO

    @PUT("notes/{id}")
    suspend fun updateNote(
        @Path("id") id: Long,
        @Header("If-Match") etag: String,
        @Body body: UpdateNoteRequestDTO,
    ): NoteResponseDTO

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: Long)
}
