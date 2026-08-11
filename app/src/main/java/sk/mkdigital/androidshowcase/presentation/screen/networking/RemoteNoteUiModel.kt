package sk.mkdigital.androidshowcase.presentation.screen.networking

import androidx.compose.runtime.Immutable
import sk.mkdigital.androidshowcase.domain.model.RemoteNote

@Immutable
data class RemoteNoteUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val etag: String,
)

fun RemoteNote.toUiModel() = RemoteNoteUiModel(
    id = id,
    title = title,
    content = content,
    etag = etag,
)
