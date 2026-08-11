package sk.mkdigital.androidshowcase.domain.repository

import sk.mkdigital.androidshowcase.domain.model.RemoteNote

interface RemoteNoteRepository {

    suspend fun getNotes(): List<RemoteNote>

    suspend fun createNote(title: String, content: String): RemoteNote

    suspend fun updateNote(id: Long, title: String, content: String, etag: String): RemoteNote

    suspend fun deleteNote(id: Long)
}
