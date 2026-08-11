package sk.mkdigital.androidshowcase.data.dto.note

import kotlinx.serialization.Serializable

@Serializable
data class CreateNoteRequestDTO(
    val title: String,
    val content: String,
)

@Serializable
data class UpdateNoteRequestDTO(
    val title: String,
    val content: String,
)
