package sk.mkdigital.androidshowcase.data.dto.note

import sk.mkdigital.androidshowcase.data.base.TransformToDomainModel
import sk.mkdigital.androidshowcase.domain.model.RemoteNote
import kotlinx.serialization.Serializable

@Serializable
data class NoteResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val etag: String,
) : TransformToDomainModel<RemoteNote> {
    override fun transform(): RemoteNote = RemoteNote(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        etag = etag,
    )
}
